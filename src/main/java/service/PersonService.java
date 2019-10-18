package service;

import model.*;
import result.PersonResult;

/**
 * Handles the functionality of retrieving all family members - Returns ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class PersonService extends Service {

    /**
     *Retrievve all family members of the current user
     * @return Response body for /person api call
     */
    PersonResult retrievePersons(){
        return null;
    }

    /**
     * Finds all the family members of the user
     * @param personID To find the ID of the user's person object
     * @return An array of all Person objects associated with the given user
     */
    Person[] retrieveRelatives(AuthToken personID){
        return null;
    }
}