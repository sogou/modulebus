package com.sogou.modulebus.functionbus;


import com.sogou.modulebus.RefUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FunctionBusTest {

    @Before
    public void setUp() throws Exception {
        FunctionBus.addFunction(LoginServiceImpl.class.getName());
        FunctionBus.addFunction(ProfileServiceImpl.class.getName());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addFunction() {

        List<String> nameList = (List<String>) RefUtil
                .getFieldByName(null, FunctionBus.class, "sNameList");

        assertTrue(nameList.contains(LoginServiceImpl.class.getName())
                && nameList.contains(ProfileServiceImpl.class.getName()));
    }

    @Test
    public void getFunctionByName(){
        Method method = RefUtil.getMethodByName(FunctionBus.class, "getFunctionByName", String.class);
        Class<?> cls1 = (Class) RefUtil.invokMethod(method, null, ILoginService.class.getName());
        Class<?> cls2 = (Class) RefUtil.invokMethod(method, null, IProfileService.class.getName());
        assertEquals(cls1, LoginServiceImpl.class);
        assertEquals(cls2, ProfileServiceImpl.class);
    }

    @Test
    public void getFunction() {
        ILoginService fun1 = FunctionBus.getFunction(ILoginService.class);
        IProfileService fun2 = FunctionBus.getFunction(IProfileService.class);
        assertNotNull(fun1);
        assertNotNull(fun2);
    }


    @Test
    public void testCache() {
        FunctionBus.getFunction(ILoginService.class);
        FunctionBus.getFunction(IProfileService.class);

        Map<String, Class<?>> map = (Map<String, Class<?>>) RefUtil
                .getFieldByName(null, FunctionBus.class, "sFunctionCacheMap");

        Class cls1 = map.get(ILoginService.class.getName());
        Class cls2 = map.get(IProfileService.class.getName());

        assertEquals(cls1, LoginServiceImpl.class);
        assertEquals(cls2, ProfileServiceImpl.class);
    }

}