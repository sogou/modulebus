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
