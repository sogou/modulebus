package com.sogou.modulebus.routerbus;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

class RealChain implements IInterceptor.Chain {

    private List<IInterceptor> interceptors;

    private RouterBuild build;

    private Object source;

    public RealChain(Object source, RouterBuild build, List<IInterceptor> interceptors){
        this.source = source;
        this.build = build;
        this.interceptors = interceptors;
    }

    @Override
    public Context getContext() {
        Context context = null;
        if (source instanceof Context) {
            context = (Context) source;
        } else if (source instanceof Fragment) {
            context = ((Fragment) source).requireContext();
        }
        return context;
    }

    @Override
    public RouterBuild getRouterBuild() {
        return build;
    }

    @Override
    public void process() {

        if (interceptors.isEmpty()){
            return;
        }else{
            try{
                IInterceptor interceptor = interceptors.remove(0);
                interceptor.interceptor(this);
            }catch (Exception e){
                if (build.isDebug()){
                    throw e;
                }else{
                    RouterCallback callback = build.getCallback();
                    if (callback != null){
                        callback.result(ResultCode.FAILED, build);
                    }
                }
            }

        }

    }
}
