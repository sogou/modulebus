package com.sogou.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.sogou.annotation.RouterSchema
import com.sogou.base.Constants
import com.sogou.base.ILoginService
import com.sogou.modulebus.functionbus.IExported

@RouterSchema(Constants.SERVICE_LOGIN)
class LoginServiceImpl : ILoginService, IExported {
    override fun login(ctx: Context?) {
        Toast.makeText(ctx, "登录成功-from module login", Toast.LENGTH_SHORT).show()
    }

    override fun dump(msg: String?) {
        Log.e("LoginService-", msg)
    }
}