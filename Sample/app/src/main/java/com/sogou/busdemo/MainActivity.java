package com.sogou.busdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.Constants;
import com.sogou.base.ILoginService;
import com.sogou.base.IProfileService;
import com.sogou.base.User;
import com.sogou.modulebus.routerbus.IInterceptor;
import com.sogou.modulebus.routerbus.ResultCode;
import com.sogou.modulebus.routerbus.RouterBuild;
import com.sogou.modulebus.routerbus.RouterBus;
import com.sogou.modulebus.routerbus.RouterCallback;

@RouterSchema({Constants.APP_SCHEMA})
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button router_concurrent, fun_login, fun_profile, fun_login_by_anno, login, anim_new, anim_old, web, tel, callback;
    Button global, get_fragment, interceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        router_concurrent = findViewById(R.id.router_concurrent);
        fun_login = findViewById(R.id.fun_login);
        fun_profile = findViewById(R.id.fun_profile);
        fun_login_by_anno = findViewById(R.id.fun_login_by_anno);
        login = findViewById(R.id.login);
        anim_new = findViewById(R.id.anim_new);
        anim_old = findViewById(R.id.anim_old);
        web = findViewById(R.id.web);
        tel = findViewById(R.id.tel);
        callback = findViewById(R.id.callback);
        global = findViewById(R.id.global);
        get_fragment = findViewById(R.id.get_fragment);
        interceptor = findViewById(R.id.interceptor);

        router_concurrent.setOnClickListener(this);
        fun_login.setOnClickListener(this);
        fun_profile.setOnClickListener(this);
        fun_login_by_anno.setOnClickListener(this);
        login.setOnClickListener(this);
        anim_new.setOnClickListener(this);
        anim_old.setOnClickListener(this);
        web.setOnClickListener(this);
        tel.setOnClickListener(this);
        callback.setOnClickListener(this);
        global.setOnClickListener(this);
        get_fragment.setOnClickListener(this);
        interceptor.setOnClickListener(this);
    }

    int count;

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.router_concurrent:
                testAnnotateConcurrent();
                testFunctionConcurrent();
                break;
            case R.id.fun_login:
                RouterBus.getInstance().navigation(ILoginService.class).login(this);
                break;
            case R.id.fun_profile:
                RouterBus.getInstance().navigation(IProfileService.class).show(this);
                break;
            case R.id.fun_login_by_anno:
                ILoginService loginService = (ILoginService) RouterBus.getInstance().build("service/login").navigation();
                loginService.login(this);

                break;
            case R.id.login:
                //使用"login://login"启动LoginActivity
                RouterBus.getInstance()
                        .build(Constants.LOGIN_SCHEMA)
                        .requestCode(66)
                        .navigation(this);
                break;
            case R.id.anim_new:
                //使用另一个注册的路由"login/login"启动LoginActivity
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                RouterBus.getInstance()
                        .build(Constants.LOGIN_SCHEMA2)
                        .withOptionsCompat(compat)
                        .navigation(this);
                break;
            case R.id.anim_old:
                RouterBus.getInstance()
                        .build(Constants.LOGIN_SCHEMA)
                        .anim(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .navigation(this);
                break;
            case R.id.web:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case R.id.tel:
                RouterBus.getInstance()
                        .build("tel:123456")
                        .setAction(Intent.ACTION_DIAL)
                        .navigation(this);

                /*RouterBus.getInstance()
                        .build("smsto:123456")
                        .setAction(Intent.ACTION_VIEW)
                        .with("sms_body", "The SMS text")
                        .navigation(this);*/

                /*RouterBus.getInstance()
                        .build("content://contacts/people")
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .navigation(this);*/
                break;

            case R.id.callback:
                /*
                 * "xx/xx/xx"为无效的路由，因此会回调ResultCode.MISSED
                 */
                Bundle bundle = new Bundle();
                bundle.putString("name", "seasonfif");
                RouterBus.getInstance()
                        .build("xx/xx/xx?a=1&b=2")
                        .requestCode(66)
                        .with(bundle)
                        .with("age", 29)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .navigation(this, new RouterCallback() {
                            @Override
                            public void result(int resultCode, RouterBuild build) {

                                switch (resultCode){
                                    case ResultCode.SUCCEED:
                                        Toast.makeText(MainActivity.this, "Router SUCCEED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ResultCode.MISSED:
//                                        Toast.makeText(MainActivity.this, "Router MISSED", Toast.LENGTH_SHORT).show();
                                        RouterBus.getInstance()
                                                .build(Constants.LOGIN_SCHEMA)
                                                .with("ResultState", "Router MISSED")
                                                .navigation(MainActivity.this);
                                        break;
                                    case ResultCode.FAILED:
                                        Toast.makeText(MainActivity.this, "Router FAILED", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                break;

            case R.id.global:
                /*
                 * "a/s/d/f"为无效的路由，且没有设置局部监听，因此会回调全局监听函数
                 */
                Bundle b = new Bundle();
                b.putString("name", "seasonfif");
                RouterBus.getInstance()
                        .build("a/s/d/f")
                        .requestCode(66)
                        .with(b)
                        .with("age", 29)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation(this);
                break;

            case R.id.get_fragment:
                Bundle b1 = new Bundle();
                b1.putString("name", "seasonfif");
                Fragment fragment = RouterBus.getInstance()
                        .build("f/tempfragment?a=1&b=2")
                        .with(b1)
                        .with("age", 29)
                        .with("user", new User("Sam", 33))
                        .getFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;

            case R.id.interceptor:
                count ++;
                IInterceptor interceptor = (IInterceptor) RouterBus.getInstance().build("/interceptor/LoginInterceptor").navigation();
                RouterBus.getInstance()
                        .build(Constants.LOGIN_SCHEMA)
                        .requestCode(66)
                        .with("count", count)
                        .addInterceptor(interceptor)
//                        .addInterceptor("/interceptor/LoginInterceptor")
                        .navigation(this, new RouterCallback() {
                            @Override
                            public void result(int resultCode, RouterBuild build) {
                                switch (resultCode){
                                    case ResultCode.INTERUPT:
//                                        Toast.makeText(MainActivity.this, "Router INTERUPTed", Toast.LENGTH_SHORT).show();
                                        RouterBus.getInstance()
                                                .build(Constants.PROFILE_SCHEMA)
                                                .with("ResultState", "Router INTERUPTED")
                                                .navigation(MainActivity.this);
                                        break;

                                    case ResultCode.FAILED:
                                        Toast.makeText(MainActivity.this, "Router FAILED", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 66){
            Toast.makeText(this, "resultCode:"+resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    private void testAnnotateConcurrent(){

        for (int i=0; i<=5; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ILoginService loginService = (ILoginService) RouterBus.getInstance().build(Constants.SERVICE_LOGIN).navigation();
                    loginService.dump(Thread.currentThread().getName());
                }
            }, "Annotate-"+i).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    IProfileService profileService = (IProfileService) RouterBus.getInstance().build(Constants.SERVICE_PROFILE).navigation();
                    profileService.dump(Thread.currentThread().getName());
                }
            }, "Annotate-"+i).start();
        }

    }

    private void testFunctionConcurrent(){

        for (int i=0; i<=5; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ILoginService loginService = RouterBus.getInstance().navigation(ILoginService.class);
                    loginService.dump(Thread.currentThread().getName());
                }
            }, "Function-"+i).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    IProfileService profileService = RouterBus.getInstance().navigation(IProfileService.class);
                    profileService.dump(Thread.currentThread().getName());
                }
            }, "Function-"+i).start();
        }
    }
}
