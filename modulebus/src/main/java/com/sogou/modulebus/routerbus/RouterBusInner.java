package com.sogou.modulebus.routerbus;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.sogou.modulebus.functionbus.FunctionBus;

import java.util.LinkedList;
import java.util.List;

class RouterBusInner extends BaseRouter{

    public RouterBusInner(){
        super();
    }

    @Override
    public IRouter addInterceptor(String schema) {
        IInterceptor interceptor;
        Class<?> aClass = RouterGlobalSetting.getInstance().getClassBySchema(schema);
        if (aClass != null && IInterceptor.class.isAssignableFrom(aClass)){
            try {
                interceptor = (IInterceptor) aClass.newInstance();
                addInterceptor(interceptor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public Fragment getFragment(){
        Fragment fragment = null;

        Object o = navigation();
        if (o instanceof Fragment){
            fragment = (Fragment) o;
            fragment.setArguments(mRouterBuild.buildBundle());
        }
        return fragment;
    }

    @Override
    public <T> T navigation(Class<T> cls){
        return FunctionBus.getFunction(cls);
    }

    @Override
    public Object navigation() {
        Object o = null;

        String schema = mRouterBuild.getSchema();
        if (Utils.textEmpty(schema)) return null;

        Class<?> aClass = RouterGlobalSetting.getInstance().getClassBySchema(schema);
        if (aClass != null){
            try {
                o = aClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return o;
    }

    @Override
    public void navigation(Context context) {
        navigation(context, null);
    }

    @Override
    public void navigation(Context context, RouterCallback callback) {

        if(context == null){
            if (callback != null){
                callback.result(ResultCode.FAILED, null);
            }
            return;
        }

        mRouterBuild.setDebug(RouterGlobalSetting.getInstance().isDebug);
        mRouterBuild.setCallback(callback);
        mRouterBuild.setGlobalDegrade(RouterGlobalSetting.getInstance().globalDegrade);

        List<IInterceptor> interceptors = new LinkedList<>();

        if (null != mRouterBuild.getInterceptors()){
            interceptors.addAll(mRouterBuild.getInterceptors());
        }

        if (RouterGlobalSetting.getInstance().globalInterceptor != null){
            interceptors.addAll(RouterGlobalSetting.getInstance().globalInterceptor);
        }
        interceptors.add(new IntentInterceptor());
        interceptors.add(new StartActivityInterceptor());

        RealChain chain = new RealChain(context, mRouterBuild, interceptors);
        chain.process();
    }

    @Override
    public void navigation(Fragment fragment) {
        navigation(fragment, null);
    }

    @Override
    public void navigation(Fragment fragment, RouterCallback callback) {
        if(fragment == null){
            if (callback != null){
                callback.result(ResultCode.FAILED, null);
            }
            return;
        }

        mRouterBuild.setDebug(RouterGlobalSetting.getInstance().isDebug);
        mRouterBuild.setCallback(callback);
        mRouterBuild.setGlobalDegrade(RouterGlobalSetting.getInstance().globalDegrade);

        List<IInterceptor> interceptors = new LinkedList<>();

        if (null != mRouterBuild.getInterceptors()){
            interceptors.addAll(mRouterBuild.getInterceptors());
        }

        if (RouterGlobalSetting.getInstance().globalInterceptor != null){
            interceptors.addAll(RouterGlobalSetting.getInstance().globalInterceptor);
        }

        interceptors.add(new IntentInterceptor());
        interceptors.add(new StartActivityInterceptor());

        RealChain chain = new RealChain(fragment, mRouterBuild, interceptors);
        chain.process();
    }
}
