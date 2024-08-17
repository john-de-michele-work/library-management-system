package com.librarymanagementsystem.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public final class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    public void setup() {
        sessionManager = new SessionManager();
    }

    @Test
    public void startSessionTest() {
        sessionManager.startSession();
        assertNotNull(sessionManager.getSession(), "Session is null!");
    }

    @Test
    public void stopSessionTest() {
        sessionManager.startSession();
        sessionManager.stopSession();
        assertNull(sessionManager.getSession(), "Session is not null!");
    }

}
