package service;

import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import result.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the functionality of retrieving of a single person - Returns the single Person object with the specified ID
 */
public class PersonIDService extends Service {

    /**
     * Retrieves a person from a person ID
     * @param personID the id of the event to be retrieved
     * @return the response body for an eventID call
     */
    public Result retrievePerson(String personID, String userName) {
        try {
            PersonIDResult personIDResult;
            Database db = new Database();
            Connection conn;
            conn = db.getConnection();
            PersonDao personDao = new PersonDao(conn);
            Person person;

            person = personDao.get(personID);
            db.closeConnection(true);

            //Check if person was found (if the user searched with a valid personID parameter
            if (person == null) {
                PersonIDResult result = new PersonIDResult();
                result.setMessage("Error: Invalid personID parameter");
                return result;
            }

            //Check if the person belongs to the user
            else if (!person.getAssociatedUsername().equals(userName)) {
                PersonIDResult result = new PersonIDResult();
                result.setMessage("Error: Requested person does not belong to this user");
                return result;
            }

            //If we reached here we're successful
            else {
                personIDResult = new PersonIDResult(person);
                return personIDResult;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Result(e.toString());
        }
    }

}
