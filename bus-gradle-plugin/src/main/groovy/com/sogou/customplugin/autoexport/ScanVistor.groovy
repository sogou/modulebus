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

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class ScanVistor extends ClassVisitor{

    /**
     * Constructs a new {@link ClassVisitor}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     */
    ScanVistor(int api, ClassVisitor cv) {
        super(api, cv)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)

        Helpers.infoList.each {
            info ->
                for (String inter : interfaces){
                    if (inter == info.scanInterface){
                        info.scanImpls.add(name)
                        System.out.println("找到了类 name: " + name)
                        System.out.println("实现的接口 interfaces: " + interfaces)
                    }
                }
        }
    }
}
