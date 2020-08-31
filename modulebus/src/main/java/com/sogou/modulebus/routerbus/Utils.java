package com.sogou.modulebus.routerbus;

public class Utils {

    public static boolean textEmpty(String text){

        return text == null || text.trim().length() <= 0;

    }

    public static String strip(String schema){
        if (Utils.textEmpty(schema)) return schema;

        String[] split = schema.split("\\?");
        if (split.length >= 1){
            return split[0];
        }

        return schema;
    }
}
