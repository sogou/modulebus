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
package com.sogou.modulebus;

import android.util.Log;

public class LogKit {

    private static final String TAG = "modulebus";

    /**
     * 日志打印级别
     */
    @LogLevel
    private static int sLevel;

    /**
     * 是否开启日志
     * @param debug
     */
    public static void init(boolean debug){
        if (debug){
            sLevel = LogLevel.LOG_W;
        }else{
            sLevel = LogLevel.LOG_NONE;
        }
    }

    /**
     * 日志打印级别设置
     * @param level
     */
    public static void init(@LogLevel int level){
        sLevel = level;
    }

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        log(tag, msg, null);
    }

    public static void log(String tag, String msg, Throwable tr) {

        if (sLevel == LogLevel.LOG_NONE) return;

        switch (sLevel){
            case LogLevel.LOG_V:
                Log.v(tag, msg, tr);
                break;
            case LogLevel.LOG_D:
                Log.d(tag, msg, tr);
                break;
            case LogLevel.LOG_I:
                Log.i(tag, msg, tr);
                break;
            case LogLevel.LOG_W:
                Log.w(tag, msg, tr);
                break;
            case LogLevel.LOG_E:
                Log.e(tag, msg, tr);
                break;
        }
    }

}
