package com.librarymanagementsystem;

import com.librarymanagementsystem.data.IssuedBook;
import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.IssuedBookResponse;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.librarymanagementsystem.BookAvailability.AVAILABLE;
import static com.librarymanagementsystem.BookAvailability.ISSUED;
import static org.junit.jupiter.api.Assertions.*;

public final class IssueBookUtilIssueBookTest {

    private final SessionManager sessionManager = new SessionManager();
    private IssueBookUtil issueBookUtil;
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
    private static final Integer GOOD_CODE = 201;
    private static final Integer UNAVAILABLE_CODE = 410;
    private static final String ERROR_MESSAGE = "error";
    private static final String GOOD_MESSAGE = "issued";
    private static final String NOT_FOUND_MESSAGE = "not found";
    private static final String UNAVAILABLE_MESSAGE = "book unavailable";
    private static final String TITLE_ERROR = "Title cannot be null or blank!";
    private static final String USER_ERROR = "User cannot be null or blank!";
    private static final String TITLE_NOT_FOUND_ERROR = "Title not found!";
    private static final String USER_NOT_FOUND_ERROR = "User not found!";
    private static final String UNAVAILABLE_ERROR = "Title unavailable, all copies issued!";

    @BeforeEach
    public void setup() {
        sessionManager.startSession();
        session = sessionManager.getSession();
        BookUtil bookUtil = new BookUtil(session);
        UserUtil userUtil = new UserUtil(session);
        issueBookUtil = new IssueBookUtil(session);
        bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        userUtil.addMinimalUser(GOOD_USER, GOOD_PHONE);
        userUtil.addMinimalUser(GOOD_USER2, GOOD_PHONE);
    }

    @Test
    public void nullTitleTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(NULL, GOOD_USER);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void nullUserTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(GOOD_TITLE, NULL);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void emptyTitleTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(EMPTY, GOOD_USER);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void emptyUserTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(GOOD_TITLE, EMPTY);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void multipleErrorsTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(NULL, EMPTY);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertTrue(response.getTitle().isEmpty(), "Title not empty!");
        assertTrue(response.getUser().isEmpty(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void userNotFoundTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(GOOD_TITLE, NOT_FOUND_USER);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title not empty!");
        assertEquals(NOT_FOUND_USER, response.getUser(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies left does not match!");
    }

    @Test
    public void titleNotFoundTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(NOT_FOUND_TITLE, GOOD_USER);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(NOT_FOUND_TITLE, response.getTitle(), "Title not empty!");
        assertEquals(GOOD_USER, response.getUser(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies left does not match!");
    }

    @Test
    public void userAndTitleNotFoundTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(NOT_FOUND_TITLE, NOT_FOUND_USER);
        assertEquals(NOT_FOUND_CODE, response.getCode(), "Code does not match!");
        assertEquals(NOT_FOUND_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(USER_NOT_FOUND_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(NOT_FOUND_TITLE, response.getTitle(), "Title not empty!");
        assertEquals(NOT_FOUND_USER, response.getUser(), "User not empty!");
        assertNull(response.getAvailability(), "Availability not null!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void bookNotAvailableTest() {
        issueBookUtil.issueBook(GOOD_TITLE, GOOD_USER);
        IssuedBookResponse response = issueBookUtil.issueBook(GOOD_TITLE, GOOD_USER2);
        assertEquals(UNAVAILABLE_CODE, response.getCode(), "Code does not match!");
        assertEquals(UNAVAILABLE_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(UNAVAILABLE_ERROR, response.getErrorMessages().getFirst(), "Error message doesn't match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title not empty!");
        assertEquals(GOOD_USER2, response.getUser(), "User not empty!");
        assertEquals(ISSUED, response.getAvailability(), "Availability does not match!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
    }

    @Test
    public void bookAvailableTest() {
        IssuedBookResponse response = issueBookUtil.issueBook(GOOD_TITLE, GOOD_USER);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertTrue(response.getErrorMessages().isEmpty(), "Error message count doesn't match!");
        assertEquals(GOOD_TITLE, response.getTitle(), "Title not empty!");
        assertEquals(GOOD_USER, response.getUser(), "User not empty!");
        assertEquals(AVAILABLE, response.getAvailability(), "Availability does not match!");
        assertEquals(0, response.getAvailableCopiesLeft(), "Available copies lft does not match!");
        List<IssuedBook> issuedBooks = session.createQuery("from IssuedBook", IssuedBook.class).getResultList();
        assertFalse(issuedBooks.isEmpty(), "Books were created from nothing!");
    }

    @AfterAll
    public static void tearDown() {
        session.close();
    }

}
