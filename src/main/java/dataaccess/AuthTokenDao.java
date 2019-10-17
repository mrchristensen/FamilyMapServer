package dataaccess;

import model.AuthToken;

/**
 * Handles all database operations for persons
 */
public class AuthTokenDao {

    /**
     * Handles a new session for a given user
     * @param userName the username of the user logging-in
     * @return the auth token of this session (tied to the user)
     */
    AuthToken startSession(String userName){
        return null;
    }
}
