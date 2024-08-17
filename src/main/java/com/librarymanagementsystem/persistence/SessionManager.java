package com.librarymanagementsystem.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SessionManager {

    private Session session;
    private final SessionFactory sessionFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);

    public SessionManager() {
        sessionFactory = SessionFactoryManager.buildSessionFactory();
    }

    /**
     * Starts a session if not already started
     */
    public void startSession() {
        if (null == session) {
            session = sessionFactory.openSession();
        }
        LOGGER.info("Session started.");
    }

    /**
     * Stops a session if not already stopped
     */
    public void stopSession() {
        if (null != session) {
            session.close();
            session = null;
        }
        LOGGER.info("Session stopped.");
    }

    public Session getSession() {
        LOGGER.info("Getting session from SessionManager.");
        return session;
    }

}
