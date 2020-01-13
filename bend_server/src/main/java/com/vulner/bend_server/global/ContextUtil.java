package com.vulner.bend_server.global;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手动获取容器
 */
@Component
public class ContextUtil implements ApplicationContextAware {
    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ContextUtil.context = context;
    }

    /**
     * 获取容器中的实例
     * @param clazz 根据class获取Spring容器中对应的Bean类
     */
    public static <T> T getBean( Class<T> clazz){
        return context.getBean(clazz);
    }

    public static ApplicationContext getContext(){
        return context;
    }
}
