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

package com.sogou.modulebus;

import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RefUtil {

    public static Object getFieldByName(Object o, Class clazz, String name){

        Object fieldObj;
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            fieldObj = field.get(o);
        } catch (Exception e) {
            e.printStackTrace();
            fieldObj = null;
        }

        return fieldObj;
    }

    public static Method getMethodByName(Class clazz, String name, Class<?>...pKey){

        Method method;
        try {
            method = clazz.getDeclaredMethod(name, pKey);
        } catch (Exception e) {
            e.printStackTrace();
            method = null;
        }

        return method;
    }

    public static Object invokMethod(Method method, Object o, Object...pValue){
        Object rtnObj = null;

        if (method != null){
            try {
                method.setAccessible(true);
                rtnObj = method.invoke(o, pValue);
            } catch (Exception e) {
                e.printStackTrace();
                rtnObj = null;
            }
        }

        return rtnObj;
    }

    public static Application getApplication(){
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method2.invoke(null, (Object[]) null);

            final Method method = activityThreadClass
                    .getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);
        }catch (Exception e){
            e.printStackTrace();
        }

        return application;
    }

}
