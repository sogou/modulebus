package com.sogou.modulebus.routerbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.sogou.modulebus.routerbus.ResultCode.FAILED;
import static com.sogou.modulebus.routerbus.ResultCode.INTERUPT;
import static com.sogou.modulebus.routerbus.ResultCode.MISSED;
import static com.sogou.modulebus.routerbus.ResultCode.SUCCEED;

@IntDef({SUCCEED, MISSED, FAILED, INTERUPT})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {

    /*
        跳转成功
     */
    int SUCCEED = 0x0001;

    /*
        未找到路由
     */
    int MISSED = 0x0002;

    /*
        跳转失败
     */
    int FAILED = 0x0003;

    /*
        被拦截器拦截
     */
    int INTERUPT = 0x0004;
}

