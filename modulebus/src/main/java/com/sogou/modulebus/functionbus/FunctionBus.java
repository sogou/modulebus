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

package com.sogou.modulebus.functionbus;

import com.sogou.modulebus.routerbus.Utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionBus {

    private static List<String> sNameList = new LinkedList<>();
    private static Map<String, Class<?>> sFunctionCacheMap = new ConcurrentHashMap<>();

    public static void addFunction(String name){
        if (!Utils.textEmpty(name)){
            sNameList.add(name);
        }
    }

    public synchronized static <T> T getFunction(Class<T> cls){

        T t = null;
        Class<?> aClass = sFunctionCacheMap.get(cls.getName());

        if (aClass == null){
            aClass = getFunctionByName(cls.getName());
        }
        try {
            t = (T) aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    private static Class getFunctionByName(String name) {

        Class clazz = null;
        Iterator<String> iterator = sNameList.iterator();
        while (iterator.hasNext()){
            String s = iterator.next();

            boolean get = false;
            try {
                Class cls = Class.forName(s);
                Class[] interfaces = cls.getInterfaces();
                for (Class c : interfaces) {
                    String iName = c.getName();
                    //过滤掉标记接口
                    if (!iName.equals(IExported.class.getName())){
                        if (iName.equals(name)){
                            clazz = cls;
                            get = true;
                        }
                        sFunctionCacheMap.put(c.getName(), cls);
                    }
                }

                //缓存过的删除
                iterator.remove();

                //如果找到目标接口则退出循环
                if (get){
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return clazz;
    }
}
