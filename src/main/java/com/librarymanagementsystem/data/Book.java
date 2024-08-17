package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Book {

    @Id
    private Long id;

    @NotNull
    @NotBlank
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

}
