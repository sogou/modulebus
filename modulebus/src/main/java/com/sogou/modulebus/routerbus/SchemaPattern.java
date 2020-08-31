package com.sogou.modulebus.routerbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Pair;

class SchemaPattern implements IPattern {
    @Override
    public Pair<Boolean, Intent> match(Context context, Uri uri, RouterBuild routerBuild) {

        if (uri == null){
            return EMPTY;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            return Pair.create(true, intent);
        }

        return EMPTY;
    }
}
