package com.librarymanagementsystem.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Entity(name = "IssuedBook")
public class IssuedBook {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    @NotEmpty
    String title;

    @NotNull
    @NotEmpty
    String user;

    Boolean isCurrent;

    public IssuedBook() {}

    public IssuedBook(String title, String user) {
        this.title = title;
        this.user = user;
        this.isCurrent = TRUE;
    }

    public Long getId() {
        return id;
    }

    public @NotNull @NotEmpty String getTitle() {
        return title;
    }

    public @NotNull @NotEmpty String getUser() {
        return user;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setNotCurrent() {
        isCurrent = FALSE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssuedBook that)) return false;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(user, that.user) &&
                Objects.equals(isCurrent, that.isCurrent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, user, isCurrent);
    }
}
