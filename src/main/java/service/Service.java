package service;

import model.Event;
import model.Person;
import model.User;

/**
 * Handles the default service functionality, parent class
 */
public class Service {
    /**
     * Inserts a person into the database
     * @param myPerson The person to insert into the database
     */
    void insertPerson(Person myPerson){
    }

    /**
     * Inserts a events into the database
     * @param myEvent The event to insert into the database
     */
    void insertEvent(Event myEvent){
    }

    /**
     * Returns all the users in the database
     * @return All the users in the database
     */
    User[] getAllUsers(){
        return null;
    }

    /**
     * Returns all the persons in the database that are associated with a given user
     * @param myUser The user to find all related Person objects
     * @return All the persons in the database that are associated with a given user
     */
    User[] getAllPersons(User myUser){
        return null;
    }

    /**
     * Returns all the events in the database that are associated with a given user
     * @param myUser The user to find all related Person objects
     * @return All the events in the database that are associated with a given user
     */
    Event[] getAllEvents(User myUser){
        return null;
    }

}
