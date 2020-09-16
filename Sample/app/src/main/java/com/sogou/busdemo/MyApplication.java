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

package com.sogou.busdemo;

import android.app.Application;
import android.util.Log;

import com.sogou.base.Constants;
import com.sogou.modulebus.routerbus.GlobalDegrade;
import com.sogou.modulebus.routerbus.RouterBuild;
import com.sogou.modulebus.routerbus.RouterBus;
import com.sogou.modulebus.routerbus.RouterFactory;

import java.util.Map;

public class MyApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        RouterBus.Builder builder = new RouterBus.Builder()
                .enableDebug(BuildConfig.DEBUG)
                .addInterceptor(new GlobalInterceptor())
                .setRouterFactory(new RouterFactory() {
                    @Override
                    public void putDynamicRouter(Map<String, String> routerTable) {
//                        routerTable.put(Constants.LOGIN_SCHEMA, "com.sogou.profile.ProfileActivity");
                    }
                })
                .setGlobalDegrade(new GlobalDegrade() {
                        @Override
                        public void onMissed(RouterBuild build) {
//                            Toast.makeText(MyApplication.this, "GlobalDegrade:"+build.getSchema(), Toast.LENGTH_SHORT).show();
                            RouterBus.getInstance()
                                    .build(Constants.LOGIN_SCHEMA)
                                    .with("GlobalDegrade", build.getSchema())
                                    .navigation(MyApplication.getApplication());
                        }
                });
        long start = System.currentTimeMillis();
        RouterBus.getInstance().init(builder);

        Log.e("RouterBus", "init cost:" + (System.currentTimeMillis() - start));
    }

    public static Application getApplication(){
        return application;
    }
}
