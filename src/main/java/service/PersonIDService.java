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
    public PersonIDResult retrievePerson(String personID, String userName) {
        PersonIDResult personIDResult = new PersonIDResult();
        Database db = new Database();
        Connection conn = null;
        try {
            conn = db.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            personIDResult.setMessage("Internal server error");
            return personIDResult;
        }
        PersonDao personDao = new PersonDao(conn);

        Person person = null;

        try {
            person = personDao.get(personID);
            db.closeConnection(true);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }

        //Check if person was found (if the user searched with a valid personID parameter
        if (person == null) {
            PersonIDResult result  = new PersonIDResult();
            result.setMessage("Invalid personID parameter");
            return result;
        }
        //Check if the person belongs to the user
        else if(person.getAssociatedUsername().equals(userName) == false){
            PersonIDResult result  = new PersonIDResult();
            result.setMessage("Requested person does not belong to this\n" +
                    "user");
            return result;
        }
        //If we reached here we're successful
        else{
            personIDResult = new PersonIDResult(person);
            return personIDResult;
        }
    }

}
