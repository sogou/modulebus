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
