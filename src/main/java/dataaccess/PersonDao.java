package dataaccess;

import model.AuthToken;
import model.Person;

/**
 * Handles all database operations for persons
 */
public class PersonDao extends Dao {
    /**
     * Adds a new person to the database
     * @param personID the id of the person to add
     * @param associatedUsername the username of the current user
     * @param firstName the first name of the new person
     * @param lastName the last name of the new person
     * @param gender the gender of the new person
     * @param spouseID the personID of the spouse of the new person
     * @param fatherID the personID of the father of the new person
     * @param motherID the personID of the mother of the new person
     */
    void insertPerson(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID){
    }

    /**
     * Delete an person from the database
     * @param personID The ID of the person to remove
     */
    void removePerson(String personID){
    }

    /**
     * Deletes all persons from the database
     */
    void removeAllPersons(){
    }

    /**
     * Retrieve a single person from the database
     * @param personID The ID of the desired person
     * @return The person object of the desired person
     */
    Person getPerson(String personID){
        return null;
    }

    /**
     * Retrieves all persons of all family of the current user (from the database)
     * @param userAuthToken The auth token of the current user
     * @return Array of all persons in the database (of all family members of user)
     */
    Person[] getAllAncestralPersons(AuthToken userAuthToken){
        return null;
    }

}
