package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {

    @Id
    private String title;

    @NotNull
    @NotBlank
    private String author;

    private Integer totalCopyCount;

    private Integer availableCopyCount;

    @NotNull
    private String isbn;

    @NotNull
    private Integer publicationYear;

    public Book() {}

    public Book(String title, String author, String isbn, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopyCount = 1;
        this.availableCopyCount = 1;
    }

    public Book(String title, String author, String isbn, Integer publicationYear, int copies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopyCount = copies;
        this.availableCopyCount = copies;
    }

}
