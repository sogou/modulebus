package com.sogou.modulebus.routerbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

class RouterTablePattern implements IPattern {
    @Override
    public Pair<Boolean, Intent> match(Context context, Uri uri, RouterBuild routerBuild) {
        Intent intent;
        Class<?> aClass = RouterGlobalSetting.getInstance().getClassBySchema(routerBuild.getSchema());
        if (aClass == null){
            return EMPTY;
        }

        if (!Activity.class.isAssignableFrom(aClass)){
            return EMPTY;
        }

        intent = new Intent(context, aClass);
        return Pair.create(true, intent);
    }
}
