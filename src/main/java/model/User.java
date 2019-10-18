package model;

import java.util.Objects;

/**
 * User account (stores information about each user's account in the database
 */
public class User {

    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Unique user name (non-empty string)
     */
    String userName;

    /**
     * User's password (non-empty string)
     */
    String password;

    /**
     * User's email address (non-empty string)
     */
    String email;

    /**
     * User's first name (non-empty string)
     */
    String firstName;

    /**
     * User's last name (non-empty string)
     */
    String lastName;

    /**
     * User's gender (string: "f" or "m")
     */
    String gender;

    /**
     * Unique Person ID assigned to this user's generated Person object (non-empty string)
     */
    String personID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender) &&
                personID.equals(user.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, email, firstName, lastName, gender, personID);
    }
}