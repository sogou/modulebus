package com.sogou.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.Constants;
import com.sogou.modulebus.routerbus.RouterBus;

import java.util.Iterator;
import java.util.Set;

@RouterSchema(Constants.PROFILE_SCHEMA)
public class ProfileActivity extends AppCompatActivity {

    TextView tv, params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("个人中心");
        params = findViewById(R.id.params);
        tv = findViewById(R.id.main);

        StringBuilder sb = new StringBuilder("个人中心：\n");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Set<String> keySet = bundle.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value;
                if (key.equals("user")){
                    value = bundle.getSerializable(key).toString();
                }else{
                    value = bundle.getString(key);
                }
                sb.append(key).append(":").append(value).append(";\n");
            }
        }
        params.setText(sb.toString());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterBus
                        .getInstance()
                        .build(Constants.APP_SCHEMA)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .navigation(ProfileActivity.this);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(888);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
