package com.sogou.modulebus.routerbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Pair;

class SysActionPattern implements IPattern {
    @Override
    public Pair<Boolean, Intent> match(Context context, Uri uri, RouterBuild routerBuild) {
        Intent intent = new Intent();

        intent.setData(uri);

        String action = routerBuild.getAction();
        if (!Utils.textEmpty(action)){
            intent.setAction(action);
        }

        String type = routerBuild.getType();
        if (!Utils.textEmpty(type)){
            intent.setAction(type);
        }

        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            return Pair.create(true, intent);
        }

        return EMPTY;
    }
}
