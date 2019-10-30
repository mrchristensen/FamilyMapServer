package result;

import model.Event;

/**
 * Response body for the /event/eventID API call
 */
public class EventIDResult extends Result {
    /**
     * Name of the user account this person belongs to
     */
    String associatedUsername;

    /**
     * Events's unique ID
     */
    String eventID;

    /**
     * Person's unique ID of whom the event belongs to
     */
    String personID;

    /**
     * Events's latitude
     */
    float latitude;

    /**
     * Event's longitude
     */
    float longitude;

    /**
     * Event's country
     */
    String country;

    /**
     * Event's city
     */
    String city;

    /**
     * Event's type
     */
    String eventType;

    /**
     * Year of the event
     */
    int year;

    /**
     * Default constructor
     */
    public EventIDResult() {
    }

    /**
     * Proper contructor for a successful response body for the /event/eventID api call
     * @param associatedUsername The Username of the current user
     * @param eventID The event's ID
     * @param personID The ID of the person who owns this event
     * @param latitude The latitude of the event
     * @param longitude The longitude of the event
     * @param country The country of the event
     * @param city The city of the event
     * @param eventType The type of event
     * @param year The year of the event
     */
    public EventIDResult(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public EventIDResult(Event event){
        associatedUsername = event.getAssociatedUsername();
        eventID = event.getEventID();
        personID = event.getPersonID();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getEventType();
        year = event.getYear();
    }
}
