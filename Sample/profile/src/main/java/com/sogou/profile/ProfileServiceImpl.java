package com.sogou.profile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.Constants;
import com.sogou.base.IProfileService;
import com.sogou.modulebus.functionbus.IExported;

@RouterSchema(Constants.SERVICE_PROFILE)
public class ProfileServiceImpl implements IProfileService, IExported {
    @Override
    public void show(Context ctx) {
//        Toast.makeText(ctx, "show profile-from module profile", Toast.LENGTH_SHORT).show();
        ctx.startActivity(new Intent(ctx, ProfileActivity.class));
    }

    @Override
    public void dump(String msg) {
        Log.e("ProfileService-", msg);
    }
}
