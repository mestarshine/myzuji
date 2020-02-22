package com.myzuji.backend.common.ioc;

import com.myzuji.util.ioc.Registry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistryWithAC extends Registry {

    /**
     * 当前容器
     */
    private static List<ApplicationContext> acList = new ArrayList<ApplicationContext>();

    @Resource
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        acList.add(applicationContext);
        Registry.setInstance(this);
    }

    @Override
    public <T> T getBean(Class<T> t) {
        T superBean = super.getBean(t);
        if (superBean != null) {
            return superBean;
        }

        for (ApplicationContext applicationContext : acList) {
            T bean = applicationContext.getBean(t);
            if (bean != null) {
                return bean;
            }
        }

        return null;
    }

    @Override
    public String getProperty(String name) {
        return environment.getProperty(name);
    }

}
