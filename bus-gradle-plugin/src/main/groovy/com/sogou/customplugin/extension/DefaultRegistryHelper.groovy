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