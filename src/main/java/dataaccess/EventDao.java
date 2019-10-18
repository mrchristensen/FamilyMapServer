package dataaccess;

import model.AuthToken;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all database operations for events
 */
public class EventDao extends Dao {
    private final Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Add a event into the database
     * @param myEvent The event to insert into the database
     */
    void insert(Event myEvent){

    }

    /**
     * Delete an event from the database
     * @param eventID The id of the event to remove
     */
    void remove(String eventID){
    }

    /**
     * Deletes all events from the database
     */
    void removeAll(){

    }

    /**
     * Retrieve a single event from the database
     * @param eventID The ID of the desired event
     * @return The event object of the desired event
     */
    Event get(String eventID){
        return null;
    }

    /**
     * Retrieves all events of all family of the current user (from the database)
     * @param userAuthToken The auth token of the current user
     * @return Array of all events in the database (of all family members of user)
     */
    Event[] getAllAncestralEvents(AuthToken userAuthToken){
        return null;
    }

}
