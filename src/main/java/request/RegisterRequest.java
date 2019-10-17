package request;

import dataaccess.UserDao;

/**
 * Pojo for the Register request body
 */
public class RegisterRequest extends Request {

    /**
     * Unique user name (non-empty string)
     */
    String username;

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
     * Default Constructor
     */
    public RegisterRequest() {
    }

    /**
     * Proper Constructor
     * @param username the username of the current user
     * @param password the password of the current user
     * @param email the email of the current user
     * @param firstName the first name of the current user
     * @param lastName the last name of the current user
     * @param gender the gender of the current user
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Generate a response body with the authToken, userName, and personID
     * @return a String of the response body
     */
    public String generateResponseBody() { return null;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
