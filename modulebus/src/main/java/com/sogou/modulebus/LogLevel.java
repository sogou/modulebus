package com.sogou.modulebus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.sogou.modulebus.LogLevel.LOG_D;
import static com.sogou.modulebus.LogLevel.LOG_E;
import static com.sogou.modulebus.LogLevel.LOG_I;
import static com.sogou.modulebus.LogLevel.LOG_NONE;
import static com.sogou.modulebus.LogLevel.LOG_V;
import static com.sogou.modulebus.LogLevel.LOG_W;

@IntDef({LOG_NONE, LOG_V, LOG_D, LOG_I, LOG_W, LOG_E})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {

    int LOG_NONE = 0x0000;

    int LOG_V = 0x0001;

    int LOG_D = 0x0002;

    int LOG_I = 0x0004;

    int LOG_W = 0x0008;

    int LOG_E = 0x000f;
}

