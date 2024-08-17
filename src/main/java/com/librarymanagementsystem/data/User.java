package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public final class User {

    @Id
    private String name;

    @NotNull
    @NotBlank
    private String phoneNumber;

    private String emailAddress;

    @org.hibernate.validator.constraints.UUID
    private UUID libraryCard;

    public User() {}

    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String phoneNumber, String emailAddress, UUID libraryCard) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.libraryCard = libraryCard;
    }

}
