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

