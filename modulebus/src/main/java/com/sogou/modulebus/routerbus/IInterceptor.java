package com.sogou.modulebus.routerbus;

import android.content.Context;

public interface IInterceptor {

    void interceptor(Chain chain);

    interface Chain{

        Context getContext();

        RouterBuild getRouterBuild();

        void process();
    }
}
