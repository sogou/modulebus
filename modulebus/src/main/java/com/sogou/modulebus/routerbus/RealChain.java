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
