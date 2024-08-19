package com.librarymanagementsystem;

import com.librarymanagementsystem.data.Book;
import com.librarymanagementsystem.data.IssuedBook;
import com.librarymanagementsystem.data.User;
import com.librarymanagementsystem.response.IssuedBookResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;

import static com.librarymanagementsystem.BookAvailability.AVAILABLE;
import static com.librarymanagementsystem.BookAvailability.ISSUED;

public final class IssueBookUtil {

    private final Session session;

    public IssueBookUtil(Session session) {
        this.session = session;
    }

    public IssuedBookResponse issueBook(String title, String user) {
        int code = 201;
        String message = "issued";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(title, user);

        if (ir.isError()) {
            return new IssuedBookResponse("", "", null, 0,
                    ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        List<User> users = session.createSelectionQuery("from Users u where u.name = :user", User.class)
                .setParameter("user", user)
                .getResultList();

        if (users.isEmpty()) {
            code = 404;
            message = "not found";
            errorMessages.add("User not found!");
            return new IssuedBookResponse(title, user, null, 0,
                    code, message, errorMessages);
        }

        List<Book> books = session.createSelectionQuery("from Book b where b.title = :title", Book.class)
                .setParameter("title", title)
                .getResultList();

        if (books.isEmpty()) {
            code = 404;
            message = "not found";
            errorMessages.add("Title not found!");
            return new IssuedBookResponse(title, user, null, 0,
                    code, message, errorMessages);
        } else if (books.getFirst().getAvailableCopyCount() == 0) {
            code = 410;
            message = "book unavailable";
            errorMessages.add("Title unavailable, all copies issued!");
            return new IssuedBookResponse(title, user, ISSUED, 0,
                    code, message, errorMessages);
        }

        books.getFirst().issueBook();
        Transaction txn = session.beginTransaction();
        session.createMutationQuery("update Book set availableCopyCount = :availableCopyCount where title = :title")
                .setParameter("availableCopyCount", books.getFirst().getAvailableCopyCount())
                .setParameter("title", books.getFirst().getTitle())
                .executeUpdate();
        txn.commit();

        session.persist(new IssuedBook(books.getFirst().getTitle(), users.getFirst().getName()));
        session.beginTransaction().commit();

        return new IssuedBookResponse(books.getFirst().getTitle(), users.getFirst().getName(), AVAILABLE,
                books.getFirst().getAvailableCopyCount(), code, message, errorMessages);
    }

    private InternalResponse errorCheck(String title, String user) {
        InternalResponse ir = new InternalResponse();

        if ((null == title) || title.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Title cannot be null or blank!");
            ir.setError();
        }

        if ((null == user) || user.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("User cannot be null or blank!");
            ir.setError();
        }

        return ir;
    }

    private static class InternalResponse {
        private int code;
        private String message;
        private final List<String> errorMessages = new LinkedList<>();
        private boolean isError = false;

        public void setCode(int code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void addErrorMessage(String errorMessage) {
            errorMessages.add(errorMessage);
        }

        public void setError() {
            isError = true;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }

        public boolean isError() {
            return isError;
        }
    }

}
