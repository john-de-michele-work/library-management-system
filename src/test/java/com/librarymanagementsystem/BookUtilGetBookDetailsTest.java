package com.librarymanagementsystem;

import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.BookDetailsResponse;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.librarymanagementsystem.BookAvailability.AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

public final class BookUtilGetBookDetailsTest {

    private final SessionManager sessionManager = new SessionManager();
    private BookUtil bookUtil;
    private static Session session;

    private static final String NULL = null;
    private static final String EMPTY = "";
    private static final String GOOD_TITLE = "A Good Title " + System.currentTimeMillis();
    private static final String NOT_FOUND_TITLE = "Not The Title You're Looking For";
    private static final String GOOD_AUTHOR = "Janice Goode";
    private static final String GOOD_ISBN = "1234567890";
    private static final Integer GOOD_PUBLICATION_YEAR = 2024;
    private static final Integer ERROR_CODE = 400;
    private static final Integer NOT_FOUND_CODE = 404;
    private static final Integer GOOD_CODE = 200;
    private static final String ERROR_MESSAGE = "error";
    private static final String GOOD_MESSAGE = "found";
    private static final String NOT_FOUND_MESSAGE = "not found";
    private static final String TITLE_ERROR = "Title cannot be null or blank!";
    private static final String NOT_FOUND_ERROR = "Title not found!";

    @BeforeEach
    public void setup() {
        sessionManager.startSession();
        session = sessionManager.getSession();
        bookUtil = new BookUtil(session);
        bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
    }

    @Test
    public void nullTitleTest() {
        BookDetailsResponse response = bookUtil.getBookDetails(NULL);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getAuthor().isEmpty(), "Author not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
    }

    @Test
    public void emptyTitleTest() {
        BookDetailsResponse response = bookUtil.getBookDetails(EMPTY);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getAuthor().isEmpty(), "Author not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
    }

    @Test
    public void titleNotFoundTest() {
        BookDetailsResponse response = bookUtil.getBookDetails(NOT_FOUND_TITLE);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        assertEquals(NOT_FOUND_TITLE, response.getTitle(), "Title does not match!");
        assertTrue(response.getAuthor().isEmpty(), "Author not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
    }

    @Test
    public void getBookDetailsTest() {
        BookDetailsResponse response = bookUtil.getBookDetails(GOOD_TITLE);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "Error message count does not match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title does not match!");
        assertEquals(GOOD_AUTHOR, response.getAuthor(), "Author does not match!");
        assertEquals(AVAILABLE, response.getAvailability(), "Availability does not match!");
    }

    @AfterAll
    public static void tearDown() {
        session.close();
    }

}
