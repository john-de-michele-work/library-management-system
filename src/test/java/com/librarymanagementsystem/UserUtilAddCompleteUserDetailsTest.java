package com.librarymanagementsystem;

import com.librarymanagementsystem.data.User;
import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.Response;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class UserUtilAddCompleteUserDetailsTest {

    private final SessionManager sessionManager = new SessionManager();
    private UserUtil userUtil;
    private static Session session;

    private static final String NULL = null;
    private static final String EMPTY = "";
    private final String goodName = "Useful User " + System.currentTimeMillis();
    private static final String GOOD_PHONE_NUMBER = "(425) 867-5309";
    private static final String GOOD_EMAIL_ADDRESS = "foo@bar.com";
    private static final String GOOD_LIBRARY_CARD = "1234567890";
    private static final Integer ERROR_CODE = 400;
    private static final Integer GOOD_CODE = 200;
    private static final String ERROR_MESSAGE = "error";
    private static final String GOOD_MESSAGE = "added";
    private static final String NAME_ERROR = "Name cannot be null or blank!";
    private static final String PHONE_ERROR = "Phone Number cannot be null or blank!";

    @BeforeEach
    public void setup() {
        sessionManager.startSession();
        session = sessionManager.getSession();
        userUtil = new UserUtil(session);
    }

    @Test
    public void nullNameTest() {
        Response response = userUtil.addCompleteUserDetails(NULL, GOOD_PHONE_NUMBER, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(NAME_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertEquals(0, users.size(), "Users were created from nothing!");
    }

    @Test
    public void nullPhoneNumberTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, NULL, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(PHONE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertEquals(0, users.size(), "Users were created from nothing!");
    }

    @Test
    public void emptyNameTest() {
        Response response = userUtil.addCompleteUserDetails(EMPTY, GOOD_PHONE_NUMBER, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(NAME_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertEquals(0, users.size(), "Users were created from nothing!");
    }

    @Test
    public void emptyPhoneNumberTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, EMPTY, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(PHONE_ERROR, response.getErrorMessages().getFirst(), "Error message does not match!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertEquals(0, users.size(), "Users were created from nothing!");
    }

    @Test
    public void multipleErrorsTest() {
        Response response = userUtil.addCompleteUserDetails(NULL, EMPTY, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(ERROR_CODE, response.getCode(), "Code does not match!");
        assertEquals(ERROR_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(2, response.getErrorMessages().size(), "Error message count does not match!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertEquals(0, users.size(), "Users were created from nothing!");
    }

    @Test
    public void addCompleteUserDetailsTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, GOOD_PHONE_NUMBER, GOOD_EMAIL_ADDRESS, GOOD_LIBRARY_CARD);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "There should be no error messages!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertFalse(users.isEmpty(), "User was not added!");
    }

    @Test
    public void addCompleteUserDetailsEmptyEmailTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, GOOD_PHONE_NUMBER, EMPTY, GOOD_LIBRARY_CARD);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "There should be no error messages!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertFalse(users.isEmpty(), "User was not added!");
    }

    @Test
    public void addCompleteUserDetailsEmptyLibraryCardTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, GOOD_PHONE_NUMBER, GOOD_EMAIL_ADDRESS, EMPTY);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "There should be no error messages!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertFalse(users.isEmpty(), "User was not added!");
    }

    @Test
    public void addCompleteUserDetailsEmptyEmailAndLibraryCardTest() {
        Response response = userUtil.addCompleteUserDetails(goodName, GOOD_PHONE_NUMBER, EMPTY, EMPTY);
        assertEquals(GOOD_CODE, response.getCode(), "Code does not match!");
        assertEquals(GOOD_MESSAGE, response.getMessage(), "Message does not match!");
        assertEquals(0, response.getErrorMessages().size(), "There should be no error messages!");
        List<User> users = session.createQuery("from User", User.class).getResultList();
        assertFalse(users.isEmpty(), "User was not added!");
    }

}
