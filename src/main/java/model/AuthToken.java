package model;

/**
 * Represents an auth token in the database
 */
public class AuthToken {

    public AuthToken(String userName, String authToken) {
        this.userName = userName;
        this.authToken = authToken;
    }

    /**
     * Username of the user logging in
     */
    String userName;

    /**
     * Unique generated auth token for this user's session
     */
    String authToken;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}