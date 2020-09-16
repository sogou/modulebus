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
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class InjectProcess {

    static void injectCode(ExportInfo info){
        List<File> destClassFiles = info.destClassFiles
        destClassFiles.each {
            file ->
                Helpers.log("需要注入的文件是："+ file.absolutePath)
                def name = file.name
                if (name.endsWith(".class")){
                    injectCodeToClass(file, info)
                }else if(name.endsWith(".jar")){
                    injectCodeToJar(file, info)
                }
        }

    }

    static void injectCodeToClass(File file, ExportInfo info){

        def optClass = new File(file.getParent(), file.name + ".opt")

        FileOutputStream outputStream = new FileOutputStream(optClass)
        InputStream inputStream = file.newInputStream()
        def bytes = doInsertCode(inputStream, info)
        outputStream.write(bytes)
        outputStream.close()
        if (file.exists()){
            file.delete()
        }

        optClass.renameTo(file)
    }

    static void injectCodeToJar(File jarFile, ExportInfo info) {
        def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
        if (optJar.exists())
            optJar.delete()
        def file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = file.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)
            if (isInitClass(info.destClass, entryName)) {
                def bytes = doInsertCode(inputStream, info)
                jarOutputStream.write(bytes)
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        file.close()

        FileUtils.copyFile(optJar, jarFile)

        if (optJar.exists()) {
            optJar.delete()
        }
    }

    static boolean isInitClass(String destClass, String entryName){
        if (entryName == null || !entryName.endsWith(".class"))
            return false
        entryName = entryName.substring(0, entryName.lastIndexOf('.'))
        return destClass == entryName
    }

    static byte[] doInsertCode(InputStream inputStream, ExportInfo info){
        Helpers.log("doInsertCode："+ info.destClass)
        ClassReader classReader = new ClassReader(inputStream)
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        InsertVistor insertVistor = new InsertVistor(Opcodes.ASM5, classWriter, info)
        classReader.accept(insertVistor, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }
}