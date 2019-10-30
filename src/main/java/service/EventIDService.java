package service;

import dataaccess.Database;
import dataaccess.EventDao;
import exceptions.DataAccessException;
import model.Event;
import model.Person;
import result.EventIDResult;
import result.EventResult;
import result.Result;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the functionality of retrieving of a single event
 */
public class EventIDService extends Service{

    /**
     * Retrieves an event from a event ID
     * @param eventID the id of the event to be retrieved
     * @return the response body for an eventID call
     */
    public Result retrieveEvent(String eventID, String userName) {
        EventIDResult eventIDResult = new EventIDResult();
        Database db = new Database();
        Connection conn = null;
        try {
            conn = db.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            eventIDResult.setMessage("Internal server error");
            return eventIDResult;
        }
        EventDao eventDao = new EventDao(conn);

        Event event = null;

        try {
            event = eventDao.get(eventID);
            db.closeConnection(true);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }

        //Check if event was found (if the user searched with a valid eventID parameter
        if (event == null) {
            Result result  = new Result();
            result.setMessage("Invalid eventID parameter");
            return result;
        }
        //Check if the event belongs to the user
        else if(event.getAssociatedUsername().equals(userName) == false){
            Result result  = new Result();
            result.setMessage("Requested event does not belong to this\n" +
                    "user");
            return result;
        }
        //If we reached here we're successful
        else{
            eventIDResult = new EventIDResult(event);
            return eventIDResult;
        }
    }

}
