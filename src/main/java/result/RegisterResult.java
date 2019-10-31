package result;

import model.*;

/**
 * Response body for the Register API call
 */
public class RegisterResult extends Result {

    /**
     * Non-empty auth token string
     */
    private String authToken;

    /**
     * User name passed in with request
     */
    private String userName;

    /**
     * Non-empty string containing the Person ID of the user’s generated Person object
     */
    private String personID;

    /**
     * Default Constructor
     */
    public RegisterResult() {
    }

    /**
     * Proper constructor to return a register response body
     * @param authToken Non-empty auth token
     * @param userName User name of the user
     * @param personID The Person ID of the user's generated Person object
     */
    public RegisterResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public RegisterResult(String toString) {
        super(toString);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthTokenString(String authToken) {
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
