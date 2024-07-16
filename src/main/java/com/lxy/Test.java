package com.lxy;

import com.lxy.service.UserService;
import com.spring.LxyApplicationContext;

public class Test {
    public static void main(String[] args) throws Exception {
        LxyApplicationContext lxyApplicationContext= new LxyApplicationContext(AppConfig.class);
        // 单例的
        UserService userService = (UserService)lxyApplicationContext.getBean("userService");
        userService.test();

    }
}
