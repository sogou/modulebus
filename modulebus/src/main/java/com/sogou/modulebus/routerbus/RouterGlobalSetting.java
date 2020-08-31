package com.sogou.modulebus.routerbus;

import com.sogou.modulebus.LogKit;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class RouterGlobalSetting {

    private static volatile RouterGlobalSetting sInstance;

    public Map<String, String> routerTable = new HashMap<>();
    public Map<String, Class<?>> cacheTable = new HashMap<>();

    public GlobalDegrade globalDegrade;

    public RouterFactory routerFactory;

    public List<IInterceptor> globalInterceptor;

    public Set<IPattern> patterns = new LinkedHashSet<>();

    public boolean isDebug;

    private RouterGlobalSetting(){
        // 初始化intent解析规则
        patterns.add(new SysActionPattern());
        patterns.add(new SchemaPattern());
        patterns.add(new RouterTablePattern());
    }

    public static RouterGlobalSetting getInstance(){
        if (sInstance == null){
            synchronized (RouterBus.class){
                if (sInstance == null){
                    sInstance = new RouterGlobalSetting();
                }
            }
        }
        return sInstance;
    }

    public void init(RouterBus.Builder builder){
        if (builder != null){
            this.isDebug = builder.isDebug();
            this.globalDegrade = builder.getGlobalDegrade();
            this.routerFactory = builder.getRouterFactory();
            this.globalInterceptor = builder.getGlobalInterceptor();
        }
    }

    /**
     * 编译时写入调用该方法的语句
     * 按照URI规范将路由schema '？'后的部分视作参数，不作为路由的校验
     * @param schema 路由名称
     * @param clazz 路由对应的类路径
     */
    public void putItem(String schema, String clazz){
        if (schema != null && clazz != null){
            routerTable.put(Utils.strip(schema), clazz);
        }
    }

    /**
     * 通过路由获取对应的class
     * @param schema
     * @return
     */
    public Class<?> getClassBySchema(String schema) {

        String strip = Utils.strip(schema);

        Class clz = cacheTable.get(strip);
        if (clz == null){

            if (routerFactory != null){
                routerFactory.putDynamicRouter(routerTable);
            }

            String clsName = routerTable.get(strip);

            try {
                clz = Class.forName(clsName);
                cacheTable.put(strip, clz);
            } catch (Exception e) {
                LogKit.log("RouterBus", "can't instantiate Router class " + clsName, e);
                e.printStackTrace();
            }
        }

        return clz;
    }
}
