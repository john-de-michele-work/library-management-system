package com.librarymanagementsystem.response;

import java.util.List;

public final class ReturnBookResponse extends Response {

    private final String title;
    private final String user;
    private final int availableCopiesLeft;

    public ReturnBookResponse(String title,
                              String user,
                              int availableCopiesLeft,
                              int code,
                              String message,
                              List<String> errorMessages) {
        super(code, message, errorMessages);
        this.title = title;
        this.user = user;
        this.availableCopiesLeft = availableCopiesLeft;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public int getAvailableCopiesLeft() {
        return availableCopiesLeft;
    }
}
