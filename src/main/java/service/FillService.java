package service;

import model.*;
import result.FillResult;

/**
 * Handles the fill functionality
 * Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server. If there is
 * any data in the database already associated with the given user name, it is deleted. The
 * optional "generations" parameter lets the caller specify the number of generations of ancestors
 * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
 * persons each with associated events).
 */
public class FillService extends Service{
    /**
     * Fills the data base with x generations of data
     * @param userName the user name of the current user
     * @param numGenerations the number of generations to generate
     * @return Response body of fill
     */
    FillResult fillGenerations(String userName, int numGenerations){
        return null;
    }
}
