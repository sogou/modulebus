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

package com.sogou.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sogou.annotation.RouterSchema
import com.sogou.base.Constants
import com.sogou.base.User
import com.sogou.modulebus.routerbus.RouterBus
import kotlinx.android.synthetic.main.activity_login.*

@RouterSchema(Constants.LOGIN_SCHEMA, Constants.LOGIN_SCHEMA2)
class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "登录"
        val sb = StringBuilder("登录成功：\n")
        val bundle = intent.extras
        if (bundle != null) {
            val keySet = bundle.keySet()
            val iterator: Iterator<String> = keySet.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = bundle[key]
                sb.append(key).append(":").append(value).append(";\n")
            }
        }
        result.setOnClickListener{ setResult(666) }

        tv.text = sb.toString()
        profile.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("name", "seasonfif")
            RouterBus.getInstance()
                    .build(Constants.PROFILE_SCHEMA)
                    .with(bundle)
                    .with("pwd", "123456")
                    .with("user", User("Sam", 33))
                    .navigation(this@LoginActivity)
        }
    }
}