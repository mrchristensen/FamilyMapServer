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
     * @param personID The ID of the user's person object
     * @return The response body for a /person api call
     */
    public PersonResult retrieveAllPersons(String associatedUsername) throws SQLException {
        PersonResult personResult = new PersonResult();
        Database db = new Database();
        Connection conn = db.openConnection();
        PersonDao personDao = new PersonDao(conn);

        Person[] data = null;
        try {
            data = personDao.getAll(associatedUsername);
        } catch (DataAccessException e) {
            e.printStackTrace();
            personResult.setMessage("Internal server error");
            db.closeConnection(false);
            return personResult;
        }

        db.closeConnection(true);
        personResult = new PersonResult(data);
        return personResult;
    }
}