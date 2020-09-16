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
