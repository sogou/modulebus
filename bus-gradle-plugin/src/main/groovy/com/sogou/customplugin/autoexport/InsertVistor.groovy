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
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InsertVistor extends ClassVisitor{

    private ExportInfo info

    /**
     * Constructs a new {@link ClassVisitor}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     */
    InsertVistor(int api, ClassVisitor cv, ExportInfo info) {
        super(api, cv)
        this.info = info
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        if (name == info.destMethod){
            Helpers.log("visitMethod: " + name)
            Helpers.log("visitMethod: " + desc)
            mv = new InsertMethodVisitor(Opcodes.ASM5, mv)
        }
        return mv
    }

    class InsertMethodVisitor extends MethodVisitor{

        /**
         * Constructs a new {@link MethodVisitor}.
         *
         * @param api
         *            the ASM API version implemented by this visitor. Must be one
         *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
         * @param mv
         *            the method visitor to which this visitor must delegate method
         *            calls. May be null.
         */
        InsertMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv)
        }

        @Override
        void visitInsn(int opcode) {

            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {

                info.scanImpls.each {
                    entity ->
                        Helpers.log("inject code: " + entity)
                        /*mv.visitTypeInsn(Opcodes.NEW, entity)
                        mv.visitInsn(Opcodes.DUP)
                        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, entity, "<init>", "()V", false)
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, info.destClass,
                                info.methodCall, "(Ljava/lang/Object;)V", false)*/

                        if (info.methodCall){
                            mv.visitLdcInsn(Helpers.convertSlashToDot(entity))
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, info.destClass, info.methodCall, "(Ljava/lang/String;)V", false)
                        }else if(info.scanInterfaceMethod){
                            mv.visitTypeInsn(Opcodes.NEW, entity)
                            mv.visitInsn(Opcodes.DUP)
                            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, entity, "<init>", "()V", false)
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, entity, info.scanInterfaceMethod, "()V", false)
                        }

                }
            }

            super.visitInsn(opcode)
        }
    }
}
