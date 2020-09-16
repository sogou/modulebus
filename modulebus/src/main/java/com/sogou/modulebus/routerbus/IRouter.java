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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

public interface IRouter {

    IRouter build(String schema);

    /**
     * Call <code>startActivityForResult</code>.
     */
    IRouter requestCode(int requestCode);

    /**
     * @see Bundle#putAll(Bundle)
     */
    IRouter with(Bundle bundle);

    /**
     * bundle.putXXX(String key, XXX value).
     */
    IRouter with(String key, Object value);

    /**
     * @see Intent#addFlags(int)
     */
    IRouter addFlags(int flags);

    /**
     * @see Intent#setType(String)
     */
    IRouter setType(String type);

    /**
     * @see Intent#setAction(String)
     */
    IRouter setAction(String action);

    /**
     * @see ActivityOptionsCompat
     */
    IRouter withOptionsCompat(ActivityOptionsCompat compat);

    /**
     * @see android.app.Activity#overridePendingTransition(int, int)
     */
    IRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim);

    IRouter addInterceptor(IInterceptor interceptor);

    IRouter addInterceptor(String schema);

    /**
     * Get an intent instance.
     *
     * @param source Activity or Fragment instance.
     * @return {@link Intent} instance.
     */
    Intent getIntent(@NonNull Object source);

    Fragment getFragment();

    <T> T navigation(Class<T> cls);

    Object navigation();

    void navigation(Context context);

    void navigation(Context context, RouterCallback callback);

    void navigation(Fragment fragment);

    void navigation(Fragment fragment, RouterCallback callback);
}
