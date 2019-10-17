package result;

import model.Event;

/**
 * Response body for the /event API call
 */
public class EventResult extends Result {
    /**
     * Array of Event objects
     */
    Event[] data;

    /**
     * Default constructor
     */
    public EventResult() {
    }

    /**
     * Proper constructor for a successful response body for the /person api call
     * @param data And array of event objects
     */
    public EventResult(Event[] data) {
        this.data = data;
    }
}
