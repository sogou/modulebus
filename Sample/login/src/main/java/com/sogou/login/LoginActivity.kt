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