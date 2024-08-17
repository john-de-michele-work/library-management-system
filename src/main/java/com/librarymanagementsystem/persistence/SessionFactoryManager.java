package com.librarymanagementsystem.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SessionFactoryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryManager.class);

    public static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage(), ex.getCause());
        }
        return null;
    }

}
