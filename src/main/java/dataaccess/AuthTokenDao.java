package dataaccess;

import model.AuthToken;

/**
 * Handles all database operations for persons
 */
public class AuthTokenDao extends Dao {

    /**
     * Handles a new session for a given user
     * @param userName the username of the user logging-in
     * @return the auth token of this session (tied to the user)
     */
    AuthToken startSession(String userName){
        return null;
    }

    /**
     * Insert an authToken into the database
     * @param myAuthToken The authToken to insert into the database
     */
    void insertAuthToken(AuthToken myAuthToken){
    }

    /**
     * Remove an authToken from the database
     * @param myAuthToken the authToken to remove from the database
     */
    void removeAuthToken(AuthToken myAuthToken){
    }

    /**
     *
     * @param userName
     * @return
     */
    AuthToken getAuthToken(String userName){
        //Todo: Handle the
        return null;
    }
}
