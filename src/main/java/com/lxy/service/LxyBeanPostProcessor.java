package com.lxy.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

@Component
public class LxyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof BeanPostProcessor) {
            return bean;
        }
        System.out.println("the before initialization bean name is " + beanName);
        if (beanName.equals("userService")) {
            ((UserService)bean).setAString("this is a test str");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof BeanPostProcessor) {
            return bean;
        }
        System.out.println("the after initialization bean name is " + beanName);
        return bean;
    }
}
