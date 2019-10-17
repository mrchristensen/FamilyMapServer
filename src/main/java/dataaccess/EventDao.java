package dataaccess;

/**
 * Handles all database operations for events
 */
public class EventDao {
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


}
