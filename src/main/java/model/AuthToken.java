package model;

import java.util.Objects;

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

    public String getAuthTokenString() {
        return authToken;
    }

    public void setAuthTokenString(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return userName.equals(authToken1.userName) &&
                authToken.equals(authToken1.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, authToken);
    }
}