package com.sogou.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.Constants;
import com.sogou.modulebus.routerbus.RouterBus;

public class LoginActivitySchema extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_schema);
        tv = findViewById(R.id.profile);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "seasonfif");
                RouterBus.getInstance()
                        .build(Constants.PROFILE_SCHEMA)
                        .with(bundle)
                        .with("pwd", "123456")
                        .navigation(LoginActivitySchema.this);
            }
        });
    }
}
