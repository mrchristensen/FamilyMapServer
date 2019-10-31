package request;

import model.Event;
import model.Person;
import model.User;

/**
 * Pojo for the Load request body
 */
public class LoadRequest extends Request {

    /**
     * Array of users to create
     */
    User[] users;

    /**
     * Array of persons to be added to the database
     */
    Person[] persons;

    /**
     * Array of events to be added to the database
     */
    Event[] events;

    /**
     * Default constructor
     */
    public LoadRequest() {
    }

    /**
     * Full constructor to create a request to "load"
     * @param users Array of users
     * @param persons Array of persons
     * @param events Array of events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
