package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity(name = "Book")
public class Book {

    @Id
    private String title;

    @NotNull
    @NotBlank
    private String author;

    private Integer totalCopyCount;

    private Integer availableCopyCount;

    @NotNull
    @NotBlank
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

    public String getTitle() {
        return title;
    }

    public @NotNull @NotBlank String getAuthor() {
        return author;
    }

    public Integer getTotalCopyCount() {
        return totalCopyCount;
    }

    public Integer getAvailableCopyCount() {
        return availableCopyCount;
    }

    public @NotNull @NotBlank String getIsbn() {
        return isbn;
    }

    public @NotNull Integer getPublicationYear() {
        return publicationYear;
    }

    public void issueBook() {
        this.availableCopyCount--;
    }

    public void returnBook() {
        this.availableCopyCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(totalCopyCount, book.totalCopyCount) &&
                Objects.equals(availableCopyCount, book.availableCopyCount) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publicationYear, book.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, totalCopyCount, availableCopyCount, isbn, publicationYear);
    }
}
