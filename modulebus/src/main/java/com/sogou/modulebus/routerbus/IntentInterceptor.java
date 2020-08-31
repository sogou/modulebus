package com.sogou.modulebus.routerbus;

import android.content.Intent;

class IntentInterceptor implements IInterceptor{

    @Override
    public void interceptor(Chain chain) {

        RouterBuild build = chain.getRouterBuild();

        Intent intent = build.getIntent(chain.getContext());

        if (intent instanceof FailWrappedIntent){
            return;
        }

        build.setIntent(intent);

        chain.process();
    }
}
