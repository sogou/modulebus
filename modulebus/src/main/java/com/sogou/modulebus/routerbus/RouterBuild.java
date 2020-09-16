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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.util.SparseArray;

import com.sogou.modulebus.LogKit;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public final class RouterBuild {

    private String schema;

    private int flags;

    private int requestCode = -1;

    private int enterAnim = -1;

    private int exitAnim = -1;

    private Bundle bundle;

    private ActivityOptionsCompat compat;

    private String action;

    private String type;

    private List<IInterceptor> interceptors;

    private Intent intent;

    private boolean isDebug;

    private RouterCallback callback;

    private GlobalDegrade globalDegrade;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getFlags() {
        return flags;
    }

    public void addFlags(int flags) {
        this.flags |= flags;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }

    public Bundle getBundle(){
        return bundle;
    }

    public ActivityOptionsCompat getOptionsCompat() {
        return compat;
    }

    public void setOptionsCompat(ActivityOptionsCompat compat) {
        this.compat = compat;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInterceptors(List<IInterceptor> interceptors){
        this.interceptors = interceptors;
    }

    public List<IInterceptor> getInterceptors(){
        return interceptors;
    }

    Intent getIntent() {
        return intent;
    }

    void setIntent(Intent intent) {
        this.intent = intent;
    }

    boolean isDebug() {
        return isDebug;
    }

    void setDebug(boolean debug) {
        isDebug = debug;
    }

    public RouterCallback getCallback() {
        return callback;
    }

    void setCallback(RouterCallback callback) {
        this.callback = callback;
    }

    GlobalDegrade getGlobalDegrade() {
        return globalDegrade;
    }

    void setGlobalDegrade(GlobalDegrade globalDegrade) {
        this.globalDegrade = globalDegrade;
    }

    public void with(Bundle bundle) {
        if (this.bundle != null){
            this.bundle.putAll(bundle);
        }else{
            this.bundle = bundle;
        }
    }

    public void with(String key, Object value){

        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (byte) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (short) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (int) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (long) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (char) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof IBinder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bundle.putBinder(key, (IBinder) value);
            } else {
                LogKit.log("putBinder() requires api 18.");
            }
        } else if (value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray<? extends Parcelable>) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else {
            LogKit.log("Unknown object type: " + value.getClass().getName());
        }
    }

    /*
    构建参数
     */
    Bundle buildBundle(){

        if (bundle == null){
            bundle = new Bundle();
        }

        //解析schema携带的参数
        if (!Utils.textEmpty(schema)){
            Uri uri = Uri.parse(schema);
            if (uri.getQuery() != null){
                Set<String> parameterNames = uri.getQueryParameterNames();
                for (String name : parameterNames) {
                    String parameter = uri.getQueryParameter(name);
                    bundle.putString(name, parameter);
                }
            }
        }

        return bundle;
    }

    /**
     * 组装参数生成最终跳转的intent
     * @param source 上下文
     * @return
     */
    public Intent getIntent(@NonNull Object source) {

        Intent intent = null;
        Context context = null;

        if (source instanceof Context) {
            context = (Context) source;
        } else if (source instanceof Fragment) {
            context = ((Fragment) source).requireContext();
        }

        String schema = getSchema();
        Uri uri = Uri.parse(schema);

        for (IPattern pattern : RouterGlobalSetting.getInstance().patterns) {
            Pair<Boolean, Intent> pair = pattern.match(context, uri, this);
            if (pair.first){
                intent = pair.second;
                break;
            }
        }

        if (intent == null){
            intent = new FailWrappedIntent();
            intent.addFlags(flags);
            Bundle bundle = buildBundle();
            intent.putExtras(bundle);

            RouterCallback callback = getCallback();
            GlobalDegrade globalDegrade = getGlobalDegrade();
            if (callback != null){
                callback.result(ResultCode.MISSED, this);
            }else {
                if (globalDegrade != null){
                    globalDegrade.onMissed(this);
                }
            }
            return intent;
        }

        intent.addFlags(flags);
        Bundle bundle = buildBundle();
        intent.putExtras(bundle);

        return intent;
    }
}
