#### ModuleBus
```
    一款简单易用的组件化解耦框架
```

#### 版本
每次升级，将在此更新最新版本号
```
bus-gradle-plugin: 1.2.3
modulebus: 1.2.7
annotationProcessor: 1.2.0
```

#### 一、功能介绍
1. 支持注解添加URI路由进行跳转，并解析参数
2. 支持跳转时设置各种参数，例如requestCode、Intent.FLAG、转场动画、Bundle参数等
3. 支持manifest->data->schema配置的跳转
4. 支持系统Action的隐式跳转
5. 支持局部和全局降级
6. 支持获取fragemnt以及传递参数
7. 支持拦截器
7. 自动处理组件运行方式
8. 提供IDE插件便捷的关联路径和目标类


#### 二、接入方法
1. 工程build.gradle中添加GradlePlugin依赖
    
    ``` gradle
    repositories {
        jcenter()
    }
    
    dependencies {
        classpath 'com.sogou.module:bus-gradle-plugin:xx.xx.xx'
    }
    ```

2. 在模块的build.gradle中配置组件化框架的版本
    ``` gradle
    implementation "com.sogou.module:modulebus:xx.xx.xx"
    ```

3. 配置注解解释器

    在需要使用注解的模块中添加注解解释器依赖

    如果该模块是普通的android module，则只需向模块的build.gradle文件中dependencies部分添加
    ```gradle
    implementation "com.sogou.module:annotationProcessor:xx.xx.xx"
    implementation "com.sogou.module:modulebus:xx.xx.xx"
    annotationProcessor "com.sogou.module:annotationProcessor:xx.xx.xx"
    ```

    如果该模块有kotlin支持，首先要在模块的build.gradle引入kapt插件
    ```gradle
    apply plugin: 'kotlin-kapt'
    ```
    然后在dependencies中增加如下依赖
    ```gradle
    implementation "com.sogou.module:annotationProcessor:xx.xx.xx"
    kapt "com.sogou.module:annotationProcessor:xx.xx.xx"
    ```

4. 使用gradle插件-bus-gradle-plugin

    在每个module的build.gradle中首行添加
    
    如果是主module添加
    ``` gradle
    ext.mainApp = true  //设置为true，表示此module为主module，以application方式编译
    apply plugin: 'module-bus-extensions'
    ```
    非主module添加
    ``` gradle
    apply plugin: 'module-bus-extensions'
    ```
    并`删除`apply plugin: 'com.android.application'或者apply plugin: 'com.android.library'


5. 模块依赖

    ```
    主module依赖的业务组件使用bus-gradle-plugin提供的addComponent方法，可以屏蔽各业务组件对主module的可见性。
    也就是说使用`implementation project(path: ":login")`，主module仍然可以直接引用login模块的代码，但是使用`addComponent "login"`，
    login模块的实现对主module却是不可见的。
    ```

#### 三、sdk初始化

1. 在Application中做sdk的初始化工作

    ```java
    RouterBus.getInstance().init(this);
    ```
    上面这种初始化不开启debug模式、不设置全局降级回调。
    
    ```java
    RouterBus.Builder builder = new RouterBus.Builder()
                .enableDebug(BuildConfig.DEBUG)
                .addInterceptor(new GlobalInterceptor())
                .setGlobalDegrade(new GlobalDegrade() {
                        @Override
                        public void onMissed(RouterBuild build) {
                            Toast.makeText(MyApplication.this, "GlobalDegrade:"+build.getSchema(), Toast.LENGTH_SHORT).show();
                        }
                });
        RouterBus.getInstance().init(this, builder);
    ```
    参数说明：
    ```
    1. enableDebug参数为true(开发阶段)会开启log输出，并会将跳转过程中的Exception抛出；
        如果为false(线上环境)则会try-catch异常，并以RouterCallback-`ResultCode.FAILED`状态回调。
    2. addInterceptor为全局拦截器，会在每次路由跳转时执行
    3. GlobalDegrade为全局降级接口，在不设置局部回调的情况下，未命中路由会回调此接口。
    ```
    

#### 四、使用路由跳转

1. 首先需要在目标Activity上使用注解标识路由
    ```
    @RouterSchema({"login://login"})
    该注解参数为数组，因此支持同一个Activity配置多个路由
    ```
    
2. 调用方示例如下：
    ``` java
    // 1. 应用内简单的跳转
    RouterBus.getInstance()
                        .build("login://login")
                        .requestCode(66)
                        .navigation(this);

    // 2. 跳转并携带参数
    Bundle bundle = new Bundle();
                bundle.putString("name", "seasonfif");
    RouterBus.getInstance()
                .build("login://login?age=29&num=215603")
                .with(bundle)
                .with("pwd", "123456")
                .with("user", new User("Sam", 33))
                .navigation();
                
    // 由该示例可以看出支持的参数有URI get参数、Bundle参数、基础类型以及序列化对象。
    
    // 3. 处理跳转结果
    // 使用两个参数的navigation方法，可以获取单次跳转的结果
    RouterBus.getInstance()
                .build("login://login")
                .navigation(this, new RouterCallback() {
                    @Override
                    public void result(int code, RouterBuild build) {
    
                        switch (code){
                            case ResultCode.SUCCEED:
                                Toast.makeText(MainActivity.this, "Router SUCCEED", Toast.LENGTH_SHORT).show();
                                break;
                            case ResultCode.MISSED:
                                Toast.makeText(MainActivity.this, "Router MISSED", Toast.LENGTH_SHORT).show();
                                break;
                            case ResultCode.FAILED:
                                Toast.makeText(MainActivity.this, "Router FAILED", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    ```
    
#### 五、服务调用
1. 使用接口类型调用
    ``` java
    // 声明接口,其他组件通过接口来调用服务
    public interface ILoginService {
        void login(Context ctx);
    }

    // 实现接口
    public class LoginServiceImpl implements ILoginService, IExported {
        @Override
        public void login(Context ctx) {
            Toast.makeText(ctx, "from module login", Toast.LENGTH_SHORT).show();
        }
    }
    ```
    实现接口时`必须`额外实现一个空接口-IExported，标识此实现类为导出调用服务。
    
    下面为接口的调用方法
    ``` java
    RouterBus.getInstance().navigation(ILoginService.class).login(this);
    ```
    如果上步中没有实现IExported标记接口，则服务不会导出，此处调用login会crash

2. 使用注解标识调用
    ``` java
    // 声明接口,其他组件通过接口来调用服务
    public interface ILoginService {
        void login(Context ctx);
    }

    // 实现接口
    @RouterSchema("service/login")
    public class LoginServiceImpl implements ILoginService {
        @Override
        public void login(Context ctx) {
            Toast.makeText(ctx, "from module login", Toast.LENGTH_SHORT).show();
        }
    }
    ```
    下面为接口的调用方法
    ``` java
    ILoginService loginService = (ILoginService) RouterBus.getInstance().build("service/login").navigation();
    loginService.login(this);
    ```
    

#### 六、获取fragment
1. 注册fragment
    ``` java
    // 与路由的注册一样，都是通过注解
    @RouterSchema("f/tempfragment")
    public class TempFragment extends Fragment {
    
    }
    ```
2. 获取fragment
    ```java
    //支持的参数有URI get参数、Bundle参数、基础类型以及序列化对象。
    Bundle b1 = new Bundle();
            b1.putString("name", "seasonfif");
    Fragment fragment = RouterBus.getInstance()
            .build("f/tempfragment?a=1&b=2")
            .with(b1)
            .with("age", 29)
            .with("user", new User("Sam", 33))
            .getFragment();
    ```
    在fragment中通过`getArguments()`来获取设置的参数。

#### 七、拦截器使用
1. 全局拦截器与局部拦截器

    顾名思义，全局拦截器对每一个跳转都起作用，而局部拦截器只对设置的地方起作用。如果同时设置了全局拦截器与局部拦截器，则会优先执行局部拦截器。
    
    全局拦截器在初始化SDK时添加，见`三、sdk初始化`
    
    局部拦截器添加如下：
    
    ```java
    // 1. 使用注解名添加
    RouterBus.getInstance()
            .build(Constants.LOGIN_SCHEMA)
            .addInterceptor("/interceptor/LoginInterceptor")
            .navigation(this);

    // 2. 使用拦截器对象添加
    IInterceptor interceptor = (IInterceptor) RouterBus.getInstance().build("/interceptor/LoginInterceptor").navigation();
    RouterBus.getInstance()
            .build(Constants.LOGIN_SCHEMA)
            .addInterceptor(interceptor)
            .navigation(this);
    
    ```
    
2. 拦截器回调
    
    如果跳转时设置了路由回调RouterCallback，则可在拦截器中获取此对象，将拦截器处理的情况回调出来。
    
    跳转代码
    ```java
    RouterBus.getInstance()
            .build(Constants.LOGIN_SCHEMA)
            .addInterceptor("/interceptor/LoginInterceptor")
            .navigation(this, new RouterCallback() {
                @Override
                public void result(int resultCode, RouterBuild build) {
                    switch (resultCode){
                        case ResultCode.INTERUPT:
                            Toast.makeText(MainActivity.this, "Router INTERUPTed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
    ```
    
    拦截器代码
    ```java
    @RouterSchema("/interceptor/LoginInterceptor")
    public class LoginInterceptor implements IInterceptor {
        @Override
        public void interceptor(Chain chain) {
            ILoginService loginService = (ILoginService) RouterBus.getInstance().build("service/login").navigation();
            loginService.login(chain.getContext());
            RouterBuild build = chain.getRouterBuild();
            // 拦截跳转 额外添加一些参数
            build.with("localinterceptor", "Interceptor");
            build.with("user", new User("Sam", 18));
            
            // 获取传递的原始参数
            Bundle bundle = build.getBundle();
            int count = bundle.getInt("count");
            if (count % 2 == 0){
                //拦截本次跳转行为，并将结果以ResultCode.INTERUPT状态回调
                RouterCallback routerCallback = build.getCallback();
                if (routerCallback != null){
                    routerCallback.result(ResultCode.INTERUPT, build);
                }
            }else{
                //不拦截跳转
                //若要继续执行请调用chain.process()
                chain.process();
            }
        }
    }
    ```
    
#### 八、动态路由

1. 动态路由工厂在初始化sdk时添加
   
    ```java
    RouterBus.Builder builder = new RouterBus.Builder()
                .enableDebug(BuildConfig.DEBUG)
                .addInterceptor(new GlobalInterceptor())
                .setRouterFactory(new RouterFactory() {
                    @Override
                    public void putDynamicRouter(Map<String, String> routerTable) {
                        // 如果key与已有的路由相同，则会覆盖已有的路由
                        routerTable.put(Constants.LOGIN_SCHEMA, "com.sogou.profile.ProfileActivity");
                    }
                });
        RouterBus.getInstance().init(this, builder);
    ```
    `注意`：为了确保动态路由的时效性，工厂方法putDynamicRouter会在每次跳转时调用，因此不要在此做耗时操作。


#### 九、开发工具

1. 插件导航到路由对应的目标类
   
    使用 AndroidStudio 手动安装插件方式安装`bus-idea-plugin.zip` ，插件位于attachment文件夹中，安装后插件无任何设置，可以在跳转代码的行首找到一个图标 (![navigation](https://git.sogou-inc.com/SogouBrowser/ModuleBus/raw/master/bus-idea-plugin/src/main/resources/icon/navi.png))
    点击该图标，即可跳转到标识了代码中路径的目标类
    
#### 十、混淆

1. 发布release版本需要添加以下混淆配置
   
    ```
    -dontwarn com.sogou.annotationprocessor.**

    -keep class com.sogou.modulebus.** { *; }
    
    -keep class * implements com.sogou.modulebus.routerbus.IInterceptor
    
    -keep class * implements com.sogou.modulebus.routerbus.IRouterRegistry
    
    -keep public class * extends com.sogou.modulebus.functionbus.IExported
    
    -keep class com.sogou.annotation.RouterSchema {
        *;
    }
    
    -keep @com.sogou.annotation.RouterSchema class * {
        *;
    }
    ```