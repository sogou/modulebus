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

package com.sogou.busdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sogou.annotation.RouterSchema;
import com.sogou.base.Constants;
import com.sogou.modulebus.routerbus.ResultCode;
import com.sogou.modulebus.routerbus.RouterBuild;
import com.sogou.modulebus.routerbus.RouterBus;
import com.sogou.modulebus.routerbus.RouterCallback;

import java.util.Iterator;
import java.util.Set;

@RouterSchema("f/testfragment")
public class TestFragment extends Fragment {

    String params = "点击跳转";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            Set<String> keySet = bundle.keySet();
            Iterator<String> iterator = keySet.iterator();
            StringBuilder sb = new StringBuilder();
            while (iterator.hasNext()){
                String key = iterator.next();
                Object value;
                if (key.equals("user")){
                    value = bundle.getSerializable(key).toString();
                }else{
                    value = bundle.get(key);
                }
                sb.append(key).append(":").append(value).append(";\n");
            }
            params = sb.toString();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView tv = new TextView(getContext());
        tv.setBackgroundColor(Color.GRAY);
        tv.setGravity(Gravity.CENTER);
        tv.setText(params);
        tv.setTextSize(20);

        final ActivityOptionsCompat compat = ActivityOptionsCompat.
                makeScaleUpAnimation(tv, tv.getWidth() / 2, tv.getHeight() / 2, 0, 0);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterBus.getInstance()
                        .build(Constants.PROFILE_SCHEMA)
                        .requestCode(88)
                        .withOptionsCompat(compat)
//                        .anim(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .navigation(TestFragment.this, new RouterCallback() {
                            @Override
                            public void result(int resultCode, RouterBuild build) {
                                switch (resultCode){
                                    case ResultCode.SUCCEED:
                                        Toast.makeText(getContext(), "Router SUCCEED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ResultCode.MISSED:
                                        Toast.makeText(getContext(), "Router MISSED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ResultCode.FAILED:
                                        Toast.makeText(getContext(), "Router FAILED", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
            }
        });
        return tv;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 88){
            Toast.makeText(getContext(), "fragment resultCode:"+resultCode, Toast.LENGTH_SHORT).show();
        }
    }
}
