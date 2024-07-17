package com.lxy.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class LxyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
//        if (bean instanceof BeanPostProcessor) {
//            return bean;
//        }
//        System.out.println("the before initialization bean name is " + beanName);
//        if (beanName.equals("userService")) {
//            ((UserService)bean).setAString("this is a test str");
//        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof BeanPostProcessor) {
            return bean;
        }
        System.out.println("the after initialization bean name is " + beanName);
        
        //AOP基于此实现
        if (beanName.equals("userService")) {
            Object proxyInstance = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        System.out.println("执行一些代理逻辑"); // 找切点对应的逻辑
                        return method.invoke(bean, args);
                    });
            return proxyInstance;
        }
        return bean;
    }
}
