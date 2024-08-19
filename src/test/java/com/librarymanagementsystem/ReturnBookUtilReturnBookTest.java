package com.librarymanagementsystem;

import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.ReturnBookResponse;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReturnBookUtilReturnBookTest {

    private final SessionManager sessionManager = new SessionManager();
    private ReturnBookUtil returnBookUtil;
    private static Session session;

    private static final String NULL = null;
    private static final String EMPTY = "";
    private static final String GOOD_TITLE = "A Good Title " + System.currentTimeMillis();
    private static final String NOT_FOUND_TITLE = "Not The Title You're Looking For";
    private static final String NOT_FOUND_USER = "Darth Vader";
    private static final String GOOD_AUTHOR = "Janice Goode";
    private static final String GOOD_ISBN = "1234567890";
    private static final Integer GOOD_PUBLICATION_YEAR = 2024;
    private static final String GOOD_USER = "Luke Skywalker";
    private static final String GOOD_USER2 = "Han Solo";
    private static final String GOOD_PHONE = "(425) 867-5309";
    private static final Integer ERROR_CODE = 400;
    private static final Integer NOT_FOUND_CODE = 404;
    private static final Integer GOOD_CODE = 200;
    private static final String ERROR_MESSAGE = "error";
    private static final String GOOD_MESSAGE = "returned";
    private static final String NOT_FOUND_MESSAGE = "not found";
    private static final String TITLE_ERROR = "Title cannot be null or blank!";
    private static final String USER_ERROR = "User cannot be null or blank!";
    private static final String NOT_FOUND_ERROR = "No active book issued for title/user!";

    @BeforeEach
    public void setup() {
        sessionManager.startSession();
        session = sessionManager.getSession();
        BookUtil bookUtil = new BookUtil(session);
        UserUtil userUtil = new UserUtil(session);
        IssueBookUtil issueBookUtil = new IssueBookUtil(session);
        returnBookUtil = new ReturnBookUtil(session);
        bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        userUtil.addMinimalUser(GOOD_USER, GOOD_PHONE);
        userUtil.addMinimalUser(GOOD_USER2, GOOD_PHONE);
        issueBookUtil.issueBook(GOOD_TITLE, GOOD_USER);
    }

    @Test
    public void nullTitleTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(NULL, GOOD_USER);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void nullUserTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(GOOD_TITLE, NULL);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void emptyTitleTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(EMPTY, GOOD_USER);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void emptyUserTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(GOOD_TITLE, EMPTY);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void multipleErrorsTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(EMPTY, NULL);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(2, response.getErrorMessages().size(), "Error message count doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void userNotIssuedBookTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(NOT_FOUND_TITLE, GOOD_USER);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(NOT_FOUND_TITLE, response.getTitle(), "Title does not match!");
        assertEquals(GOOD_USER, response.getUser(), "User does not match!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void bookNotIssuedToUserTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(GOOD_TITLE, GOOD_USER2);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title does not match!");
        assertEquals(GOOD_USER2, response.getUser(), "User does not match!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void returnBookTest() {
        ReturnBookResponse response = returnBookUtil.returnBook(GOOD_TITLE, GOOD_USER);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertTrue(response.getErrorMessages().isEmpty(), "Error message count doesn't match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title does not match!");
        assertEquals(GOOD_USER, response.getUser(), "User does not match!");
        assertEquals(1, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @AfterAll
    public static void tearDown() {
        session.close();
    }

}
