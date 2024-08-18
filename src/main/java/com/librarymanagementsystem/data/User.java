package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity(name = "User")
public class User {

    @Id
    private String name;

    @NotNull
    @NotBlank
    private String phoneNumber;

    private String emailAddress;

    private String libraryCard;

    public User() {}

    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String phoneNumber, String emailAddress, String libraryCard) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.libraryCard = libraryCard;
    }

    public String getName() {
        return name;
    }

    public @NotNull @NotBlank String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getLibraryCard() {
        return libraryCard;
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
