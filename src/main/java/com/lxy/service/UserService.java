package com.lxy.service;

import com.spring.Autowired;
import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.InitializingBean;

@Component("userService")
public class UserService implements BeanNameAware, InitializingBean, User {
    @Autowired
    private OrderService orderService;

    private String beanName;

    private String string;

    public void test() {
        System.out.println(orderService);
        System.out.println("the bean's name:" + beanName);
        System.out.println(string);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("userService初始化");
    }

    @Override
    public void proxyTest() {
        System.out.println("实现user接口");
    }

    @Override
    public void proxyTest2() {
        System.out.println("实现user接口2");
    }

    // beanPostProcessor测试时使用
//    public void setAString(String string) {
//        this.string = string;
//    }
}
