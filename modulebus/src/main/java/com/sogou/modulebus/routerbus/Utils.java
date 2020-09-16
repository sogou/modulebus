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
