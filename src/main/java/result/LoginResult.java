package result;

import model.AuthToken;

/**
 * Response body for the Login API call
 */
public class LoginResult extends Result {

    /**
     * Non-empty auth token
     */
    private String authToken;

    /**
     * Unique user name (non-empty string)
     */
    private String userName;

    /**
     * Non-empty string containing the Person Id of the user's generated Person object
     */
    private String personID;

    public LoginResult() {
    }

    /**
     * Proper constructor to generate a general response message
     *
     * @param message the string that will be interpreted as the message
     */
    public LoginResult(String message) {
        super(message);
    }

    /**
     * Proper constructor to generate a response message for the login api call
     * @param authToken authToken of this user's current session
     * @param userName username of the user
     * @param personID Person ID of the user's Person object
     */
    public LoginResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthTokenString() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
