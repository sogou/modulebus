/*
 * Copyright (c) 2020 Sogou, Inc.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.sogou.modulebus.routerbus;

import com.sogou.modulebus.LogKit;

import java.util.LinkedList;
import java.util.List;

public class RouterBus{

    private static volatile RouterBus sInstance;

    private RouterBus(){
    }

    public static class Builder{

        private List<IInterceptor> globalInterceptor = new LinkedList<>();

        private GlobalDegrade globalDegrade;

        private RouterFactory routerFactory;

        private boolean isDebug;

        /**
         * 全局降级处理
         * 如果设置了局部降级，则全局降级不生效
         * @param globalDegrade
         * @return
         */
        public Builder setGlobalDegrade(GlobalDegrade globalDegrade) {
            this.globalDegrade = globalDegrade;
            return this;
        }

        /**
         * 开启debug
         * @param debug
         * @return
         */
        public Builder enableDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        /**
         * 添加全局拦截器
         * 全局拦截器优先级小于局部拦截器
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(IInterceptor interceptor){
            globalInterceptor.add(interceptor);
            return this;
        }

        /**
         * 设置动态路由的生成工厂
         * 每次跳转都会检查动态路由设置
         * @param routerFactory
         * @return
         */
        public Builder setRouterFactory(RouterFactory routerFactory) {
            this.routerFactory = routerFactory;
            return this;
        }

        public RouterFactory getRouterFactory() {
            return routerFactory;
        }

        public GlobalDegrade getGlobalDegrade() {
            return globalDegrade;
        }

        public boolean isDebug() {
            return isDebug;
        }

        public List<IInterceptor> getGlobalInterceptor(){
            return globalInterceptor;
        }

    }

    public void init(){
        init(null);
    }

    public void init(Builder builder){
        boolean debug = false;
        if (builder != null){
            debug = builder.isDebug;
        }

        LogKit.init(debug);
        RouterGlobalSetting.getInstance().init(builder);
    }

    public static RouterBus getInstance(){
        if (sInstance == null){
            synchronized (RouterBus.class){
                if (sInstance == null){
                    sInstance = new RouterBus();
                }
            }
        }
        return sInstance;
    }

    public IRouter build(String schema) {
        RouterBusInner busInner = new RouterBusInner();
        busInner.build(schema);
        return busInner;
    }

    public <T> T navigation(Class<T> cls){
        T t = new RouterBusInner().navigation(cls);
        return t;
    }
}
