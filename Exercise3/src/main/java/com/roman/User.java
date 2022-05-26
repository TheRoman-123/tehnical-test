package com.roman;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String userName;

    public User(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName) && lastName.equals(user.lastName) && userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, userName);
    }
}
