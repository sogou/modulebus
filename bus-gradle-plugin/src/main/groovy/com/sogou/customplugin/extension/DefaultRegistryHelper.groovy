package com.sogou.customplugin.extension

import com.sogou.customplugin.autoexport.Helpers

/**
 * ModuleBus框架自身注册
 */
class DefaultRegistryHelper {

    static void addDefaultRegistry(ArrayList<ExportInfo> list) {

        /*
        若class A implements IRouterRegistry
        在RouterBus的构造函数中注入(new A()).register()
        * */
        addDefaultRegistryFor(list,
                'com.sogou.modulebus.routerbus.IRouterRegistry',
                'register',
                'com.sogou.modulebus.routerbus.RouterBus',
                "<init>",
                null)

        /*
        若class B implements IExported
        在FunctionBus的静态初始化块中注入addFunction("com.xxx.B")
        * */
        addDefaultRegistryFor(list,
                'com.sogou.modulebus.functionbus.IExported',
                null,
                'com.sogou.modulebus.functionbus.FunctionBus',
                "<clinit>",
                'addFunction')
    }

    private static void addDefaultRegistryFor(ArrayList<ExportInfo> list, String scanInterface,
                            String scanInterfaceMethod, String destClass, String destMethod, String methodCall) {
        ExportInfo info = new ExportInfo()
        info.scanInterface = scanInterface
        info.scanInterfaceMethod = scanInterfaceMethod
        info.destClass = destClass //代码注入的类
        info.methodCall = methodCall //生成的代码所调用的方法
        info.destMethod = destMethod
        info.format()
        Helpers.log(info.toString())
        list.add(info)
    }
}