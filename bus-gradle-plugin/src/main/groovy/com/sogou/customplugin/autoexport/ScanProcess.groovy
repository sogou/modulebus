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


import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class ScanProcess{

    static void scanJarFile(File src, File dest){

        def srcPath = src.absolutePath

        JarFile jarFile = new JarFile(src)
        Enumeration enumeration = jarFile.entries()
        while (enumeration.hasMoreElements()){
            JarEntry entry = enumeration.nextElement()
            String entryName = entry.name
//            Helpers.log("entryName:" + entryName)  //com/seasonfif/base/BuildConfig.class
            if (Helpers.isClassFile(entryName)){
                def className = entryName.substring(0, entryName.lastIndexOf("."))
                //寻找初始化的类
                /*if (className == Helpers.getDestClass()){
                    Helpers.log("className:" + className) //com/seasonfif/base/function/FunctionBus
                    Helpers.fileContainsInitClass = dest
                }*/
                Helpers.infoList.each {
                    info ->
                        if (className == info.destClass){
                            Helpers.log("className:" + className) //com/seasonfif/base/function/FunctionBus
                            info.destClassFiles.add(dest)
                        }
                }

                //寻找需要被注册的类（实现了特殊接口）
                def classNameWithType = Helpers.getClassNameWithType(entryName)
                if (!Helpers.isIgnoreClass(classNameWithType)){
                    InputStream inputStream = jarFile.getInputStream(entry)
                    visitClass(inputStream, src.absolutePath)
                    inputStream.close()
                }
            }
        }
    }

    /*
    扫描源码文件
     */
    static void scanClassFile(String entryName, File src, File dest){
        def name = src.name
        if (Helpers.isClassFile(name) && !Helpers.isSysClass(entryName) && !Helpers.isIgnoreClass(name)){
//            Helpers.log("src.name-" + name)
            entryName = entryName.substring(0, entryName.lastIndexOf("."))
            /*if (entryName == Helpers.getDestClass()){
//                Helpers.log("entryName-" + entryName)
                Helpers.fileContainsInitClass = dest
            }*/

            //寻找是不是需要往里写入的目标类
            Helpers.infoList.each {
                info ->
                    if (entryName == info.destClass){
                        Helpers.log("className:" + className) //com/seasonfif/base/function/FunctionBus
                        info.destClassFiles.add(dest)
                    }
            }

            //寻找该类是不是实现特殊标记接口，待动态写入的类
            if (!Helpers.isIgnoreClass(name)){
                InputStream inputStream = src.newInputStream()
                visitClass(inputStream, src.absolutePath)
                inputStream.close()
            }
        }
    }


    static void visitClass(InputStream inputStream, String filePath) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanVistor cv = new ScanVistor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        inputStream.close()
    }
}