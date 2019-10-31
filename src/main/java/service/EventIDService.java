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
        try {
            EventIDResult eventIDResult;
            Database db = new Database();
            Connection conn;
            Event event;
            conn = db.getConnection();
            EventDao eventDao = new EventDao(conn);
            event = eventDao.get(eventID);
            db.closeConnection(true);

            //Check if event was found (if the user searched with a valid eventID parameter
            if (event == null) {
                Result result = new Result();
                result.setMessage("Error: Invalid eventID parameter");
                return result;
            }
            //Check if the event belongs to the user
            else if (!event.getAssociatedUsername().equals(userName)) {
                Result result = new Result();
                result.setMessage("Error: Requested event does not belong to this user");
                return result;
            }
            //If we reached here we're successful
            else {
                eventIDResult = new EventIDResult(event);
                return eventIDResult;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Result(e.toString());
        }
    }

}
