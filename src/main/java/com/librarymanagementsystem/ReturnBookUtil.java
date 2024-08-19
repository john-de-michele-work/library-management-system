package com.librarymanagementsystem;

import com.librarymanagementsystem.data.Book;
import com.librarymanagementsystem.data.IssuedBook;
import com.librarymanagementsystem.response.ReturnBookResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;

public final class ReturnBookUtil {

    private final Session session;

    public ReturnBookUtil(Session session) {
        this.session = session;
    }

    public ReturnBookResponse returnBook(String title, String user) {
        int code = 200;
        String message = "returned";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(title, user);

        if (ir.isError()) {
            return new ReturnBookResponse("", "", 0,
                    ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        List<IssuedBook> issuedBooks = session
                .createSelectionQuery("from IssuedBook ib where ib.title = :title and ib.user = :user and ib.isCurrent = true", IssuedBook.class)
                .setParameter("user", user)
                .setParameter("title", title)
                .getResultList();

        if (issuedBooks.isEmpty()) {
            code = 404;
            message = "not found";
            errorMessages.add("No active book issued for title/user!");
            return new ReturnBookResponse(title, user, 0, code, message, errorMessages);
        }

        Transaction txn = session.beginTransaction();
        session.createMutationQuery("update IssuedBook set isCurrent = false where title = :title and user = :user and isCurrent = true")
                .setParameter("title", title)
                .setParameter("user", user)
                .executeUpdate();
        txn.commit();

        List<Book> books = session.createSelectionQuery("from Book b where b.title = :title", Book.class)
                .setParameter("title", title)
                .getResultList();

        int updatedAvailabilityCount = books.getFirst().getAvailableCopyCount() + 1;

        txn = session.beginTransaction();
        session.createMutationQuery("update Book set availableCopyCount = :availableCopyCount where title = :title")
                .setParameter("availableCopyCount", updatedAvailabilityCount)
                .setParameter("title", title)
                .executeUpdate();
        txn.commit();

        return new ReturnBookResponse(title, user, updatedAvailabilityCount, code, message, errorMessages);

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
