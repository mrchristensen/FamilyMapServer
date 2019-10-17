package model;

/**
 * Describes an event in the database
 */
public class Event {

    public Event(String eventID, String associatedUsername, String personID, int latitude, int longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Unique identifier for this event (non-empty string)
     */
    String eventID;

    /**
     * User (Username) to which this person belongs
     */
    String associatedUsername;

    /**
     * ID of person to which this event belongs
     */
    String personID;

    /**
     * Latitude of event’s location
     */
    int latitude;

    /**
     * Longitude of event’s location
     */
    int longitude;

    /**
     * Country in which event occurred
     */
    String country;

    /**
     * City in which event occurred
     */
    String city;

    /**
     * Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    String eventType;

    /**
     * Year in which event occurred
     */
    int year;
}
