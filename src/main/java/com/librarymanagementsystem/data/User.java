package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

@Entity
public class User {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(name, user.name) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(emailAddress, user.emailAddress) &&
                Objects.equals(libraryCard, user.libraryCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, emailAddress, libraryCard);
    }
}
