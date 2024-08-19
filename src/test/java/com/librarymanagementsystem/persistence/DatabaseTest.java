package com.librarymanagementsystem.persistence;

import com.librarymanagementsystem.data.Book;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test to verify that the schema is being created
 */
public final class DatabaseTest {

    private Session session;

    @BeforeEach
    public void setup() {
        SessionManager sessionManager = new SessionManager();
        sessionManager.startSession();
        session = sessionManager.getSession();
    }

    @Test
    public void queryTest() {
        List<Book> books = session.createQuery("from Book", Book.class).getResultList();
        assertTrue(books.size() > 1, "Books were created from nothing!");
    }

    @AfterEach
    public void teardown() {
        session.close();
    }
}
