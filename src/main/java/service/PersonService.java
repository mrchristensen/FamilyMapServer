package service;

import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import result.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the functionality of retrieving all family members - Returns ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class PersonService extends Service {

    /**
     * All family members of the user
     * @param associatedUsername The ID of the user's person object
     * @return The response body for a /person api call
     */
    public Result retrieveAllPersons(String associatedUsername) {
        try {
            PersonResult personResult;
            Database db = new Database();
            Connection conn = db.getConnection();
            PersonDao personDao = new PersonDao(conn);

            Person[] data;
            data = personDao.getAll(associatedUsername);

            db.closeConnection(true);
            personResult = new PersonResult(data);
            return personResult;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Result(e.toString());
        }
    }
}