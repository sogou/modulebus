package com.sogou.modulebus.routerbus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

interface IPattern {

    Pair<Boolean, Intent> EMPTY = Pair.create(false, null);

    Pair<Boolean, Intent> match(Context context, Uri uri, RouterBuild routerBuild);

}
