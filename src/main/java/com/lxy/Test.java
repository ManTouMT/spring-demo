package com.lxy;

import com.lxy.service.User;
import com.lxy.service.UserService;
import com.spring.LxyApplicationContext;

public class Test {
    public static void main(String[] args) throws Exception {
        LxyApplicationContext lxyApplicationContext= new LxyApplicationContext(AppConfig.class);
        // 单例的
        User userService = (User) lxyApplicationContext.getBean("userService");
        userService.proxyTest();
        System.out.println("-------");
        userService.proxyTest2();
//        userService.test();

    }
}
