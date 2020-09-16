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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import com.sogou.base.Constants;
import com.sogou.base.User;
import com.sogou.busdemo.BuildConfig;
import com.sogou.busdemo.MainActivity;
import com.sogou.busdemo.R;
import com.sogou.busdemo.WebActivity;
import com.sogou.modulebus.functionbus.FunctionBus;
import com.sogou.modulebus.functionbus.ILoginService;
import com.sogou.modulebus.functionbus.LoginServiceImpl;
import com.sogou.modulebus.functionbus.ProfileServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 19)
public class RouterBusTest {

    IRouter router;
    MainActivity activity;

    @Before
    public void setUp() throws Exception {
        FunctionBus.addFunction(LoginServiceImpl.class.getName());
        FunctionBus.addFunction(ProfileServiceImpl.class.getName());

        RouterGlobalSetting.getInstance().putItem("/main", MainActivity.class.getName());
        RouterGlobalSetting.getInstance().putItem("/web", WebActivity.class.getName());
        RouterGlobalSetting.getInstance().putItem("/login", "com.sogou.login.LoginActivity");
        RouterGlobalSetting.getInstance().putItem("/profile", "com.sogou.profile.ProfileActivity");


        activity = Robolectric.buildActivity(MainActivity.class).get();
        Bundle bundle = new Bundle();
        bundle.putString("name", "seasonfif");
        router = RouterBus.getInstance()
                .build("/web?a=1&b=2")
                .requestCode(66)
                .with(bundle)
                .with("age", 29)
                .anim(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void callRouterRegister() {
        registerRouter();
        Class<?> aClass = RouterGlobalSetting.getInstance().getClassBySchema(Constants.APP_SCHEMA);
        assertEquals(aClass.getName(), "com.sogou.busdemo.MainActivity");
    }

    @Test
    public void getClassBySchema() {
        Class<?> aClass = RouterGlobalSetting.getInstance().getClassBySchema("/main?a=1&b=2");
        print("getClassBySchema", aClass.getName());
        assertEquals(aClass.getName(), "com.sogou.busdemo.MainActivity");
    }

    @Test
    public void getIntent() {
        Intent intent = router
                .getIntent(Robolectric.buildActivity(MainActivity.class).get());

        print("getIntent", intent.toUri(Intent.URI_INTENT_SCHEME));
        assertTrue(intent.toUri(Intent.URI_INTENT_SCHEME).contains(WebActivity.class.getSimpleName()));
    }

    @Test
    public void getBundle() {
        Intent intent = router
                .getIntent(Robolectric.buildActivity(MainActivity.class).get());
        Bundle b = intent.getExtras();
        print("getBundle", b.toString());
        assertEquals("seasonfif", b.getString("name"));
        assertEquals(29, b.get("age"));
        assertEquals("1", b.get("a"));
        assertEquals("2", b.get("b"));
    }

    @Test
    public void getMissedBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("namea", "seasonfif");
        RouterBus.getInstance()
                .build("/weba?a=11&b=22")
                .requestCode(66)
                .with(bundle)
                .with("age", 29)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation(activity, new RouterCallback() {
                    @Override
                    public void result(int resultCode, RouterBuild build) {

                        Bundle b = build.getBundle();
                        print("getMissedBundle", "resultCode:" + resultCode + ";" + b.toString());
                        assertEquals("seasonfif", b.getString("namea"));
                        assertEquals(29, b.get("age"));
                        assertEquals("11", b.get("a"));
                        assertEquals("22", b.get("b"));
                    }
                });
    }

    @Test
    public void addInterceptor(){
        registerRouter();
        RouterBus.getInstance()
                .build("/web?a=11&b=22")
                .with("name", "seasonfif")
                .addInterceptor("/testInterceptor")
                .navigation(activity, new RouterCallback() {
                    @Override
                    public void result(int resultCode, RouterBuild build) {
                        assertEquals(ResultCode.INTERUPT, resultCode);
                        assertTrue(build.getBundle().getBoolean("interceptor"));
                    }
                });
    }

    @Test
    public void addInterceptor2(){
        registerRouter();
        IInterceptor interceptor = (IInterceptor) RouterBus.getInstance().build("/testInterceptor").navigation();
        RouterBus.getInstance()
                .build("/web?a=11&b=22")
                .with("name", "seasonfif")
                .addInterceptor(interceptor)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation(activity, new RouterCallback() {
                    @Override
                    public void result(int resultCode, RouterBuild build) {
                        assertEquals(ResultCode.INTERUPT, resultCode);
                        assertEquals(build.getFlags(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        assertTrue(build.getBundle().getBoolean("interceptor"));
                    }
                });
    }

    @Test
    public void getFragment() {
        registerRouter();
        Bundle b1 = new Bundle();
        b1.putString("name", "seasonfif");
        Fragment fragment = RouterBus.getInstance()
                .build("f/testfragment?a=1&b=2")
                .with(b1)
                .with("age", 29)
                .with("user", new User("Sam", 33))
                .getFragment();
        assertNotNull(fragment);
        Bundle arguments = fragment.getArguments();
        print("getFragment", arguments.toString());
        print("getFragment", fragment.getView()+"");
        SupportFragmentTestUtil.startFragment(fragment);
        print("getFragment", fragment.getView().toString());
    }

    @Test
    public void navigation() {
        router.navigation(activity);
        assertEquals(Shadows.shadowOf(activity).getNextStartedActivity()
                .getComponent().getClassName(), WebActivity.class.getName());
    }

    @Test
    public void navigationFunction() {
        Object o = RouterBus.getInstance().navigation(ILoginService.class);
        assertEquals(o.getClass().getName(), LoginServiceImpl.class.getName());
    }

    @Test
    public void testAction(){
        try{
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeBasic();
            RouterBus.getInstance()
                    .build("tel:123456")
                    .setAction(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .withOptionsCompat(compat)
                    .navigation(activity, new RouterCallback() {
                        @Override
                        public void result(int resultCode, RouterBuild build) {
                            Bundle b = build.getBundle();
                            Intent it = build.getIntent();
                            print("testAction", "resultCode:" + resultCode + ";" + b.toString());
                            assertEquals(it.getAction(), Intent.ACTION_SEND);
                            assertEquals(it.getType(), "text/plain");
                        }
                    });
        }catch(Exception e){

        }
    }

    @Test
    public void fragmentNavigation() {
        Fragment fragment = new Fragment();
        SupportFragmentTestUtil.startFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString("name", "seasonfif");
        RouterBus.getInstance()
                .build("/web?a=1&b=2")
                .requestCode(66)
                .with(bundle)
                .with("age", 29)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation(fragment, new RouterCallback() {
                    @Override
                    public void result(int resultCode, RouterBuild build) {
                        print("fragmentNavigation", resultCode+"");
                        assertEquals(1, resultCode);
                    }
                });

        assertEquals(Shadows.shadowOf(fragment.getActivity()).getNextStartedActivity()
                .getComponent().getClassName(), WebActivity.class.getName());
    }

    @Test
    public void fragmentNavigation2() {
        Fragment fragment = new Fragment();
        SupportFragmentTestUtil.startFragment(fragment);
        RouterBus.getInstance()
                .build("/web?a=1&b=2")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation(fragment);

        assertEquals(Shadows.shadowOf(fragment.getActivity()).getNextStartedActivity()
                .getComponent().getClassName(), WebActivity.class.getName());
    }

    static void print(String tag, String s){
        System.out.println(tag + ":" + s);
    }

    private void registerRouter() {

        String[] modules = {"app", "login", "profile", "base"};
        String prefix = "com.sogou.modulebus.routerbus.RouterRegistry_";

        for (String s : modules) {
            String clsName = prefix + s;
            registerByName(clsName);
        }

    }

    private void registerByName(String clsName) {

        try {
            Class<?> aClass = Class.forName(clsName);

            if (IRouterRegistry.class.isAssignableFrom(aClass)){
                IRouterRegistry registry = (IRouterRegistry) aClass.newInstance();
                registry.register();
            }
        } catch (Exception e) {
            print("registerByName", e.getMessage());
        }
    }
}