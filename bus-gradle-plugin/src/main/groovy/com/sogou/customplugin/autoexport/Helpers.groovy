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

package com.sogou.customplugin.autoexport

import com.sogou.customplugin.extension.ExportInfo

class Helpers{

    public static List<ExportInfo> infoList = new ArrayList<>()

    static void log(String log){
        System.out.println(log)
    }

    /**过滤所有非module的依赖库
        使用androidX设置android.enableJetifier=true时的路径为：
        /Users/apple/.gradle/caches/transforms-1/files-1.1/jetified-modulebus-1.2.5-SNAPSHOT.aar/7c1294824685c6d34f023709ef57b262/jars/classes.jar
        未开启时：
        /Users/apple/.gradle/caches/transforms-1/files-1.1/modulebus-1.2.5-SNAPSHOT.aar/3481040a0134f6489939d7bd88daed67/jars/classes.jar
    */
    static boolean shouldScan(String path){
        return !path.contains("/.gradle/caches/") || path.contains("modulebus-")
    }

    static boolean isClassFile(String name){
        return name.endsWith(".class")
    }

    //过滤系统的class
    static boolean isSysClass(String path){
        return path.startsWith("android/") || path.startsWith("androidx/")
    }

    static boolean isIgnoreClass(String name){
        return name.contains("R\$") || name == "BuildConfig.class" || name == "R.class"
    }

    static String convertDotToSlash(String str) {
        return str ? str.replaceAll('\\.', '/').intern() : str
    }

    static String convertSlashToDot(String str) {
        return str ? str.replaceAll('/', '\\.').intern() : str
    }

    static boolean isUnix(){
        return File.separator.equalsIgnoreCase('/')
    }

    static String getClassNameWithType(String path){
        String name
        if (isUnix()){
            name = path.substring(path.lastIndexOf(File.separator) + 1, path.length())
        }else{
            path = path.replaceAll('/', '\\\\')
            name = path.substring(path.lastIndexOf(File.separator) + 1 , path.length())
//            Helpers.log("path:" + path)
        }
//        Helpers.log("ClassNameWithType:" + name)
        return name
    }

    static void reset(){
        infoList.each {
            it.reset()
        }
    }

}