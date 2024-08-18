package com.librarymanagementsystem;

import com.librarymanagementsystem.data.Book;
import com.librarymanagementsystem.response.BookDetailsResponse;
import com.librarymanagementsystem.response.Response;
import org.hibernate.Session;

import java.util.*;

import static com.librarymanagementsystem.BookAvailability.AVAILABLE;
import static com.librarymanagementsystem.BookAvailability.ISSUED;

public final class BookUtil {

    private final Session session;

    public BookUtil(Session session) {
        this.session = session;
    }

    public Response addSingleBook(String title, String author, String isbn, Integer publicationYear) {
        int code = 200;
        String message = "added";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(title, author, isbn, publicationYear);

        if (ir.isError()) {
            return new Response(ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        session.persist(new Book(title, author, isbn, publicationYear));
        session.beginTransaction().commit();
        return new Response(code, message, errorMessages);
    }

    public Response addMultipleBooks(String title, String author, String isbn, Integer publicationYear, int copies) {
        int code = 200;
        String message = "added";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(title, author, isbn, publicationYear);

        if (ir.isError()) {
            if (copies < 1) {
                ir.addErrorMessage("Number of copies must be at least 1!");
            }
            return new Response(ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        if (copies < 1) {
            errorMessages.add("Number of copies must be at least 1!");
            return new Response(400, "error", errorMessages);
        }

        session.persist(new Book(title, author, isbn, publicationYear, copies));
        session.beginTransaction().commit();
        return new Response(code, message, errorMessages);
    }

    public BookDetailsResponse getBookDetails(String title) {
        int code = 200;
        String message = "found";
        List<String> errorMessages = new LinkedList<>();

        if ((null == title) || title.isBlank()) {
            code = 400;
            message = "error";
            errorMessages.add("Title cannot be null or blank!");
            return new BookDetailsResponse("", "", null, code, message, errorMessages);
        }

        List<Book> books = session.createSelectionQuery("from Book b where b.title = :title", Book.class)
                .setParameter("title", title)
                .getResultList();

        if (books.isEmpty()) {
            code = 404;
            message = "not found";
            errorMessages.add("Title not found!");
            return new BookDetailsResponse(title, "", null, code, message, errorMessages);
        } else {
            Book book = books.getFirst();
            BookAvailability availability = (book.getAvailableCopyCount() > 0) ? AVAILABLE : ISSUED;
            return new BookDetailsResponse(book.getTitle(), book.getAuthor(), availability, code, message, errorMessages);
        }
    }

    private InternalResponse errorCheck(String title,
                               String author,
                               String isbn,
                               Integer publicationYear) {

        InternalResponse ir = new InternalResponse();

        if ((null == title) || title.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Title cannot be null or blank!");
            ir.setError();
        }

        if ((null == author) || author.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Author cannot be null or blank!");
            ir.setError();
        }

        if ((null == isbn) || isbn.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("ISBN cannot be null or blank!");
            ir.setError();
        }

        if (null == publicationYear) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Publication Year cannot be null!");
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
