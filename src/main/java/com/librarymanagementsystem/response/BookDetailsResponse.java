package com.librarymanagementsystem.response;

import com.librarymanagementsystem.BookAvailability;

import java.util.List;

public final class BookDetailsResponse extends Response {

    private final BookAvailability availability;
    private final String title;
    private final String author;

    public BookDetailsResponse(String title,
                               String author,
                               BookAvailability availability,
                               int code,
                               String message,
                               List<String> errorMessages) {
        super(code, message, errorMessages);
        this.availability = availability;
        this.title = title;
        this.author = author;
    }

    public BookAvailability getAvailability() {
        return availability;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
