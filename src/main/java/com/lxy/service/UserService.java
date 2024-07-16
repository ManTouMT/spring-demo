package com.lxy.service;

import com.spring.Component;
import com.spring.Scope;

@Component("userService")
public class UserService {
    @Autowired
    private OrderService orderService;
    
    public void test() {
        System.out.println(orderService);
    }
}
