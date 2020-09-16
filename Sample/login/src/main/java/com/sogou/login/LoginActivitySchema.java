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
