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

package com.sogou.modulebus;

import com.sogou.modulebus.functionbus.FunctionBus;
import com.sogou.modulebus.functionbus.LoginServiceImpl;
import com.sogou.modulebus.functionbus.ProfileServiceImpl;

public class RefTest {

    public static void main(String[] args) {
        FunctionBus.addFunction(LoginServiceImpl.class.getName());
        FunctionBus.addFunction(ProfileServiceImpl.class.getName());

        Object nameList = RefUtil.getFieldByName(null, FunctionBus.class, "sNameList");

        print(nameList.toString());
    }

    static void print(String s){
        System.out.print(s);
    }

}
