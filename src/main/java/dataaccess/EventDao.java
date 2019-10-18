package dataaccess;

import model.AuthToken;
import model.Event;

/**
 * Handles all database operations for events
 */
public class EventDao extends Dao {
    /**
     * Add a event into the data base
     * @param associatedUsername the username of the current user
     * @param personID the id of the person who's event this is
     * @param eventType the type of this new event
     * @param city the city of this new event
     * @param latitude the latitude of this new event
     * @param longitude the longitude of this new event
     * @param country the country of this new event
     * @param year the year of this new event
     */
    void insertEvent(String associatedUsername, String personID, int latitude, int longitude, String country, String city, String eventType, int year){
    }

    /**
     * Delete an event from the database
     * @param eventID
     */
    void removeEvent(String eventID){
    }

    /**
     * Deletes all events from the database
     */
    void removeAllEvents(){

    }

    /**
     * Retrieve a single event from the database
     * @param eventID The ID of the desired event
     * @return The event object of the desired event
     */
    Event getEvent(String eventID){
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
