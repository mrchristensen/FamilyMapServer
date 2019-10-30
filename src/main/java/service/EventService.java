package service;

import dataaccess.Database;
import dataaccess.EventDao;
import exceptions.DataAccessException;
import handlers.JsonDeserialization;
import model.Event;
import result.EventResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the functionality of retrieving all events of all family members
 * (Returns ALL events for ALL family members of the current user. The current
 * user is determined from the provided auth token.)
 */
public class EventService extends Service{
    /**
     * All family members of the user
     * @param personID The ID of the user's person object
     * @return The response body for a /event api call
     */
    public EventResult retrieveAllEvents(String associatedUsername) throws SQLException {
        EventResult eventResult = new EventResult();
        Database db = new Database();
        Connection conn = db.openConnection();
        EventDao eventDao = new EventDao(conn);

        Event[] data = null;
        try {
            data = eventDao.getAll(associatedUsername);
        } catch (DataAccessException e) {
            e.printStackTrace();
            eventResult.setMessage("Internal server error");
            db.closeConnection(false);
            return eventResult;
        }

        db.closeConnection(true);
        eventResult = new EventResult(data);
        return eventResult;
    }

}
