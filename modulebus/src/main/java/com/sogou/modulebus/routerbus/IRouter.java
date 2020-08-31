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
