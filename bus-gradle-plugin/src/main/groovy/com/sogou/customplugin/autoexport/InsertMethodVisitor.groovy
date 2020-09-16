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

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class InsertMethodVisitor extends AdviceAdapter {

    private String methodName

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     * @param mv
     *            the method visitor to which this adapter delegates calls.
     * @param access
     *            the method's access flags (see {@link Opcodes}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     */
    protected InsertMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc)
        this.methodName = name
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        System.out.println(methodName + ": onMethodEnter")
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        System.out.println(methodName + ": onMethodExit")
    }
}
