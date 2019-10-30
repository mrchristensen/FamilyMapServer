package request;

/**
 * Pojo for the Login request body
 */
public class LoginRequest extends Request {

    /**
     * Unique user name (non-empty string)
     */
    String userName;

    /**
     * User's password (non-empty string)
     */
    String password;

    /**
     * Default constructor
     */
    public LoginRequest() {
    }

    /**
     * Proper constructor
     * @param username The username of the current user
     * @param password The password of the current user
     */
    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
