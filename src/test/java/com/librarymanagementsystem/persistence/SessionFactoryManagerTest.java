package com.librarymanagementsystem.persistence;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class SessionFactoryManagerTest {

    @Test
    public void buildSessionFactoryTest() {
        SessionFactory sessionFactory = SessionFactoryManager.buildSessionFactory();
        assertNotNull(sessionFactory, "Session Factory is null!");
    }

}
