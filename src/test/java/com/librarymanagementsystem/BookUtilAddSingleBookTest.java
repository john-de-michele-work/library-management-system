package com.librarymanagementsystem;

import com.librarymanagementsystem.data.Book;
import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.Response;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class BookUtilAddSingleBookTest {

    private final SessionManager sessionManager = new SessionManager();
    private BookUtil bookUtil;
    private static Session session;

    private static final String NULL = null;
    private static final String EMPTY = "";
    private static final String GOOD_TITLE = "A Good Title " + System.currentTimeMillis();
    private static final String GOOD_AUTHOR = "Janice Goode";
    private static final String GOOD_ISBN = "1234567890";
    private static final Integer GOOD_PUBLICATION_YEAR = 2024;
    private static final Integer ERROR_CODE = 400;
    private static final Integer GOOD_CODE = 201;
    private static final String ERROR_MESSAGE = "error";
    private static final String GOOD_MESSAGE = "added";
    private static final String TITLE_ERROR = "Title cannot be null or blank!";
    private static final String AUTHOR_ERROR = "Author cannot be null or blank!";
    private static final String ISBN_ERROR = "ISBN cannot be null or blank!";
    private static final String PUBLICATION_YEAR_ERROR = "Publication Year cannot be null!";

    @BeforeEach
    public void setup() {
        sessionManager.startSession();
        session = sessionManager.getSession();
        bookUtil = new BookUtil(session);
    }

    @Test
    public void nullTitleTest() {
        Response response = bookUtil.addSingleBook(NULL, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void nullAuthorTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, NULL, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(AUTHOR_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void nullIsbnTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, NULL, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(ISBN_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void nullPublicationYearTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, GOOD_ISBN, null);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(PUBLICATION_YEAR_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void emptyTitleTest() {
        Response response = bookUtil.addSingleBook(EMPTY, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(TITLE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void emptyAuthorTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, EMPTY, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(AUTHOR_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void emptyIsbnTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, EMPTY, GOOD_PUBLICATION_YEAR);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(ISBN_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
    }

    @Test
    public void multipleErrorsTest() {
        Response response = bookUtil.addSingleBook(NULL, NULL, EMPTY, null);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(4, response.getErrorMessages().size(), "Error message count does not match!");
    }

    @Test
    public void addSingleBookTest() {
        Response response = bookUtil.addSingleBook(GOOD_TITLE, GOOD_AUTHOR, GOOD_ISBN, GOOD_PUBLICATION_YEAR);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "There should be no error messages!");
        List<Book> books = session.createQuery("from Book", Book.class).getResultList();
        assertTrue(books.size() > 1, "Book was not created!");
    }

    @AfterAll
    public static void tearDown() {
        session.close();
    }

}
