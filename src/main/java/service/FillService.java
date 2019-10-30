package service;

import dataaccess.Database;
import dataaccess.PersonDao;
import dataaccess.UserDao;
import exceptions.DataAccessException;
import generation.Generation;
import model.*;
import result.FillResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
    public FillResult fillGenerations(String userName, int numGenerations) throws SQLException {
        FillResult myResult = new FillResult();

        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao userDao = new UserDao(conn);

        //Todo: make sure that numGenerations is valid
        try {
            User baseUser = userDao.get(userName);
            if(baseUser != null){
                PersonDao personDao = new PersonDao(conn);
                Person baseChild = personDao.get(baseUser.getPersonID());
                db.closeConnection(true);
                Generation generation = new Generation();
                generation.genGenerations(baseChild, numGenerations);

                //TODO: count the num of events (local var of Generation?)
                int X = 0;
                int Y = 0;
                myResult.setMessage("Successfully added " + X + " persons and " + Y + " events to the database.");
            }
            else{
                myResult.setMessage("Invalid username");
            }
        } catch (DataAccessException e) {
            myResult.setMessage("Invalid username");
        } catch (IOException e){
            myResult.setMessage("Invalid numGenerations");
        }


        return myResult;
    }
}
