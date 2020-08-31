package com.sogou.customplugin.extension

import com.sogou.customplugin.autoexport.Helpers;

class ExportInfo {

    String scanInterface

    String scanInterfaceMethod

    String destClass

    //    默认静态块
    String destMethod = "<clinit>"

    String methodCall

    List<File> destClassFiles = new ArrayList<>()

    List<String> scanImpls = new ArrayList<>()

    void format(){
        if (scanInterface){
            scanInterface = Helpers.convertDotToSlash(scanInterface)
        }

        if (destClass){
            destClass = Helpers.convertDotToSlash(destClass)
        }
    }

    boolean isValidate(){
        return scanInterface && destClass
    }

    void reset(){
        destClassFiles.clear()
        scanImpls.clear()
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append('{\n')
        sb.append('\tscanInterface : ').append(scanInterface).append(',\n')
        sb.append('\tscanInterfaceMethod : ').append(scanInterfaceMethod).append(',\n')
        sb.append('\tdestClass : ').append(destClass).append(',\n')
        sb.append('\tdestMethod : ').append(destMethod).append(',\n')
        sb.append('\tmethodCall : ').append(methodCall).append('\n')
        sb.append('}')
        return sb.toString()
    }
}
