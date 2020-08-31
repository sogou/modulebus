package com.sogou.login;

import android.os.Bundle;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.ILoginService;
import com.sogou.base.User;
import com.sogou.modulebus.routerbus.IInterceptor;
import com.sogou.modulebus.routerbus.ResultCode;
import com.sogou.modulebus.routerbus.RouterBuild;
import com.sogou.modulebus.routerbus.RouterBus;
import com.sogou.modulebus.routerbus.RouterCallback;

@RouterSchema("/interceptor/LoginInterceptor")
public class LoginInterceptor implements IInterceptor {
    @Override
    public void interceptor(Chain chain) {

        ILoginService loginService = (ILoginService) RouterBus.getInstance().build("service/login").navigation();
        loginService.login(chain.getContext());

        RouterBuild build = chain.getRouterBuild();
        build.with("localinterceptor", "Interceptor");
        build.with("user", new User("Sam", 18));
        Bundle bundle = build.getBundle();
        int count = bundle.getInt("count");
        if (count % 2 == 0){
            RouterCallback routerCallback = build.getCallback();
            if (routerCallback != null){
                routerCallback.result(ResultCode.INTERUPT, build);
            }
        }else{
            chain.process();
        }
    }
}
