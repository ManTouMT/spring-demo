package com.lxy.service;

import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.InitializingBean;
import com.spring.Scope;

@Component("userService")
public class UserService implements BeanNameAware, InitializingBean {
    @Autowired
    private OrderService orderService;
    
    private String beanName;
    
    public void test() {
        System.out.println(orderService);
        System.out.println("the bean's name:" + beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化");
    }
}
