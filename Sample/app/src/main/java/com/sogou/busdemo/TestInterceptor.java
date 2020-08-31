package com.sogou.busdemo;

import android.os.Bundle;

import com.sogou.annotation.RouterSchema;
import com.sogou.modulebus.routerbus.IInterceptor;
import com.sogou.modulebus.routerbus.ResultCode;
import com.sogou.modulebus.routerbus.RouterBuild;
import com.sogou.modulebus.routerbus.RouterCallback;

@RouterSchema("/testInterceptor")
public class TestInterceptor implements IInterceptor {
    @Override
    public void interceptor(Chain chain) {
        RouterBuild build = chain.getRouterBuild();
        Bundle bundle = build.getBundle();
        bundle.putBoolean("interceptor", true);

        RouterCallback callback = build.getCallback();
        if (callback != null){
            callback.result(ResultCode.INTERUPT, build);
        }
    }
}
