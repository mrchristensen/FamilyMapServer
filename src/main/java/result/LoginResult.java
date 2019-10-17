package result;

import model.AuthToken;

/**
 * Response body for the Login API call
 */
public class LoginResult extends Result {

    /**
     * Non-empty auth token
     */
    AuthToken authToken;

    /**
     * Unique user name (non-empty string)
     */
    String userName;

    /**
     * Non-empty string containing the Person Id of the user's generated Person object
     */
    String personID;

    public LoginResult() {
    }

    /**
     * Proper constructor to generate a response message for the login api call
     * @param authToken authToken of this user's current session
     * @param userName username of the user
     * @param personID Person ID of the user's Person object
     */
    public LoginResult(AuthToken authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
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
