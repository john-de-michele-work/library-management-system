package com.librarymanagementsystem.response;

import com.librarymanagementsystem.BookAvailability;

import java.util.List;

public final class IssuedBookResponse extends Response {

    private final BookAvailability availability;
    private final String title;
    private final String user;
    private final Integer availableCopiesLeft;

    public IssuedBookResponse(String title,
                              String user,
                              BookAvailability availability,
                              Integer availableCopiesLeft,
                              int code,
                              String message,
                              List<String> errorMessages) {
        super(code, message, errorMessages);
        this.title = title;
        this.user = user;
        this.availability = availability;
        this.availableCopiesLeft = availableCopiesLeft;
    }

    public BookAvailability getAvailability() {
        return availability;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public Integer getAvailableCopiesLeft() {
        return availableCopiesLeft;
    }
}
