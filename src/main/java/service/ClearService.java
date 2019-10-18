package service;

import result.ClearResult;

/**
 * Handles the clear functionality - Delete all data from the database (user accounts, auth tokens, persons, and events)
 */
public class ClearService extends Service{
    /**
     * Clears the database
     * @return Clear return response body
     */
    ClearResult clearDatabase(){
        return null;
    }
}
