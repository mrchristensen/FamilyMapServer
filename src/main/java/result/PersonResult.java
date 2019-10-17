package result;

import model.Person;

/**
 * Response body for the /person API call
 */
public class PersonResult extends Result {
    /**
     * Array of Person objects
     */
    Person[] data;

    /**
     * Default constructor
     */
    public PersonResult() {
    }

    /**
     * Proper constructor for a successful response body for the /person api call
     * @param data And array of person objects
     */
    public PersonResult(Person[] data) {
        this.data = data;
    }
}
