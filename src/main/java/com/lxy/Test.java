package com.lxy;

import com.spring.LxyApplicationContext;

public class Test {
    public static void main(String[] args) throws Exception {
        LxyApplicationContext lxyApplicationContext= new LxyApplicationContext(AppConfig.class);
        // 单例的
        Object userService = lxyApplicationContext.getBean("userService");
        System.out.println(userService);
        Object userService2 = lxyApplicationContext.getBean("userService");
        System.out.println(userService2);
        //原型的
        Object userService3 = lxyApplicationContext.getBean("userService");
        System.out.println(userService3);
        Object userServicet = lxyApplicationContext.getBean("userService2");
        System.out.println(userServicet);
        Object userServicet2 = lxyApplicationContext.getBean("userService2");
        System.out.println(userServicet2);
        Object userServicet3 = lxyApplicationContext.getBean("userService2");
        System.out.println(userServicet3);
        

    }
}
