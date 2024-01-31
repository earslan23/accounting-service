package com.accounting.ilab.util;

/*
 * @createdAt 31.01.2024/01/2024 - 02:06
 * @package   com.accounting.ilab.util
 * @author    fmss
 */

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil {

    @Autowired
    private ApplicationContext applicationContext;

    public <T> T getBeanOfType(Class<T> beanType) {
        // Obtain the bean from the application context
        Object bean = applicationContext.getBean(beanType);

        // If the bean is a proxy, resolve the target class
        Class<?> ultimateTargetClass = AopProxyUtils.ultimateTargetClass(bean);

        // Ensure that the bean is of the expected type
        if (beanType.isAssignableFrom(ultimateTargetClass)) {
            return beanType.cast(bean);
        } else {
            throw new IllegalStateException("Bean type mismatch");
        }


    }
}