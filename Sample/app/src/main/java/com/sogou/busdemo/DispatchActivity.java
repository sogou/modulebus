package com.sogou.busdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sogou.annotation.RouterSchema;
import com.sogou.modulebus.routerbus.RouterBus;

@RouterSchema({"/uri/dispatcher"})
public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        if (uri != null){
            String schema = uri.getQueryParameter("uri");
            RouterBus.getInstance().build(schema).navigation(this);
        }
        finish();
    }
}
