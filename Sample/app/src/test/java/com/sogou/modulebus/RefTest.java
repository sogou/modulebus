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
