package com.sogou.busdemo;

import com.sogou.modulebus.routerbus.IInterceptor;
import com.sogou.modulebus.routerbus.RouterBuild;

public class GlobalInterceptor implements IInterceptor {
    @Override
    public void interceptor(Chain chain) {
        RouterBuild build = chain.getRouterBuild();

        build.with("interceptor", "GlobalInterceptor");

        chain.process();
    }
}
