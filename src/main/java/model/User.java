package model;

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

}