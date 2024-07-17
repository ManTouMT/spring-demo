package com.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LxyApplicationContext {
    private Class<?> configClass;
    
    private ConcurrentHashMap<String, Object> singletonMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 不保证顺序
    private List<BeanPostProcessor> beanPostProcessorList  = new ArrayList<>();
    
    public LxyApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        // 解析配置类
        // componentScan注解--》扫描路径--》扫描---》beanDefinition---》beanDefinitionMap
        scan(configClass);
        //   启动的时候创建好单例bean
        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()){
            BeanDefinition beanDefinition = entry.getValue();
            Object bean = createBean(entry.getKey(), beanDefinition);
            singletonMap.put(entry.getKey(), bean);
        }

    }
    
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            
            //依赖注入
            for(Field field : clazz.getDeclaredFields()){
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(field.getName());
                    Autowired declaredAnnotation = field.getDeclaredAnnotation(Autowired.class);
                    if (bean == null && declaredAnnotation.required()) {
                        throw new RuntimeException("Required @Autowired annotation not found");
                    }
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            //aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }
            
            // 初始化
            if(instance instanceof InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }
            //后置处理器 beanPostProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }
            
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void scan(Class<?> configClass) {
        ComponentScan componentScanAnnotation = 
                configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value();
        path = path.replace(".","/");
        //扫描
        ClassLoader classLoader = LxyApplicationContext.class.getClassLoader(); //app
        URL resource = classLoader.getResource(path);// 相对target下的目录
        File file = new File(Objects.requireNonNull(resource).getFile());
        if (file.isDirectory()) {
            for (File files : Objects.requireNonNull(file.listFiles())) {
                String fileName = files.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    // 替换
                    // fileName G:\JavaProjects\spring-demo\target\classes\com\lxy\service\UserService.class
                    // className com.lxy.service.UserService
                    String className = fileName.substring(fileName.indexOf("com"), fileName.lastIndexOf(".class"));
                    className = className.replace("\\", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        // 表示当前这个类是一个bean
                        if (clazz.isAnnotationPresent(Component.class)) {
    
                            // 表示实现了beanPostProcessor
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }
                            
                            // 解析类，判断当前bean是单例bean还是原型bean
                            Component declaredAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = declaredAnnotation.value();
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    }catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                            IllegalAccessException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                
            }
        }
    }

    public Object getBean(String beanName) {
        if(beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                return singletonMap.get(beanName);
            } else {
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException("beanName not in the definitionMap:" + beanName);
        }
        
    }
}