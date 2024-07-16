package com.spring;

public class LxyApplicationContext {
    private Class<?> configClass;
    
    public LxyApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        // 解析配置类
        
    }
    
    public Object getBean(String beanName) {
        return null;
    }
}