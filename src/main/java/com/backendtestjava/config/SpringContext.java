package com.backendtestjava.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Object getBean(String qualifierName) {
        return context.getBean(qualifierName);
    }

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        // store ApplicationContext reference to access required beans later on
        SpringContext.context = context;
    }
}
