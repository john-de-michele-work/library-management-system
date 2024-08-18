package com.librarymanagementsystem.persistence;

import com.librarymanagementsystem.data.Book;
import com.librarymanagementsystem.data.IssuedBook;
import com.librarymanagementsystem.data.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
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
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(IssuedBook.class)
                    .getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage(), ex.getCause());
        }
        return null;
    }

}
