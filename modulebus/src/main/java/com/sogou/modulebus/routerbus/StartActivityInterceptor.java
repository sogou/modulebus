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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.sogou.modulebus.LogKit;

class StartActivityInterceptor implements IInterceptor {

    @Override
    public void interceptor(Chain chain) {

        RouterBuild build = chain.getRouterBuild();
        Context context = chain.getContext();
        Intent intent = build.getIntent();
        RouterCallback callback = build.getCallback();
        boolean isDebug = build.isDebug();

        try {

            Bundle options = build.getOptionsCompat() != null ? build.getOptionsCompat().toBundle() : null;
            if (context instanceof Activity){
                int requestCode = build.getRequestCode();
                Activity activity = (Activity) context;
                if (requestCode > 0){
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
                }else{
                    ActivityCompat.startActivity(activity, intent, options);
                }

                if (build.getEnterAnim() >= 0 && build.getExitAnim() >= 0) {
                    activity.overridePendingTransition(
                            build.getEnterAnim(), build.getExitAnim());
                }
            }else{
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityCompat.startActivity(context, intent, options);
            }
            if (callback != null){
                callback.result(ResultCode.SUCCEED, build);
            }
        }catch (Exception e){
            if (isDebug){
                LogKit.log("RouterBus", "navigation failed", e);
                throw e;
            }else{
                if (callback != null){
                    callback.result(ResultCode.FAILED, build);
                }
            }
        }
    }
}
