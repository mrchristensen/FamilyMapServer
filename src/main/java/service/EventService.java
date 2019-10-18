package service;

import model.Event;
import result.EventResult;

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
    EventResult retrieveAllEvents(String personID) {
        return null;
    }

    public Event[] getEvents() {
        return null; }
}
