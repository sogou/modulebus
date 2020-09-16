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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;

import java.util.LinkedList;
import java.util.List;

abstract class BaseRouter implements IRouter{

    RouterBuild mRouterBuild;

    BaseRouter(){
        mRouterBuild = new RouterBuild();
    }

    @Override
    public IRouter build(String schema) {
        mRouterBuild.setSchema(schema);
        return this;
    }

    @Override
    public IRouter requestCode(int requestCode) {
        mRouterBuild.setRequestCode(requestCode);
        return this;
    }

    @Override
    public IRouter with(Bundle bundle) {
        mRouterBuild.with(bundle);
        return this;
    }

    @Override
    public IRouter with(String key, Object value) {
        mRouterBuild.with(key, value);
        return this;
    }

    @Override
    public IRouter withOptionsCompat(ActivityOptionsCompat compat){
        mRouterBuild.setOptionsCompat(compat);
        return this;
    }

    @Override
    public IRouter addFlags(int flags) {
        mRouterBuild.addFlags(flags);
        return this;
    }

    @Override
    public IRouter setType(String type) {
        mRouterBuild.setType(type);
        return this;
    }

    @Override
    public IRouter setAction(String action) {
        mRouterBuild.setAction(action);
        return this;
    }

    @Override
    public IRouter anim(int enterAnim, int exitAnim) {
        mRouterBuild.setEnterAnim(enterAnim);
        mRouterBuild.setExitAnim(exitAnim);
        return this;
    }

    @Override
    public IRouter addInterceptor(IInterceptor interceptor) {

        List<IInterceptor> interceptors = mRouterBuild.getInterceptors();
        if (interceptors == null){
            interceptors = new LinkedList<>();
        }

        if (interceptor != null){
            interceptors.add(interceptor);
        }
        mRouterBuild.setInterceptors(interceptors);
        return this;
    }

    @Override
    public Intent getIntent(@NonNull Object source) {
        return mRouterBuild.getIntent(source);
    }
}
