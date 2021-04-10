package com.myzuji.util.ioc;

import java.util.HashMap;
import java.util.Map;

public class Registry {

    private static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

    private static Map<String, String> propertiesMap = new HashMap<String, String>();

    private static Registry instance = new Registry();

    public static Registry getInstance() {
        return instance;
    }

    public static void setInstance(Registry aInstance) {
        instance = aInstance;
    }

    public static <T> T queryBean(Class<T> t) {
        return getInstance().getBean(t);
    }

    public static void addBean(Object obj) {
        if (obj == null) {
            return;
        }

        Class<? extends Object> class1 = obj.getClass();
        while (class1 != null) {
            addBean(class1, obj);
            class1 = class1.getSuperclass();
        }
    }

    public static String queryProperty(String name) {
        return getInstance().getProperty(name);
    }

    public static void addBean(Class<?> class1, Object obj) {
        beanMap.put(class1, obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> t) {
        return (T) beanMap.get(t);
    }

    public String getProperty(String name) {
        return propertiesMap.get(name);
    }
}
