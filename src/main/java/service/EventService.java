package service;

import dataaccess.Database;
import dataaccess.EventDao;
import exceptions.DataAccessException;
import model.Event;
import result.EventResult;
import result.Result;

import java.sql.Connection;

/**
 * Handles the functionality of retrieving all events of all family members
 * (Returns ALL events for ALL family members of the current user. The current
 * user is determined from the provided auth token.)
 */
public class EventService extends Service{
    /**
     * All family members of the user
     * @param associatedUsername The ID of the user's person object
     * @return The response body for a /event api call
     */
    public Result retrieveAllEvents(String associatedUsername) {
        try {
            EventResult eventResult;
            Database db = new Database();
            Connection conn = db.getConnection();
            EventDao eventDao = new EventDao(conn);

            Event[] data;
            data = eventDao.getAll(associatedUsername);

            db.closeConnection(true);
            eventResult = new EventResult(data);
            return eventResult;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Result(e.toString());
        }
    }

}
