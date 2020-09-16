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
