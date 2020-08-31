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
