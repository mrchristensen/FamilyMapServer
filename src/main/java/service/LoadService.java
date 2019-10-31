package service;

import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import request.*;
import result.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the load functionality
 * Clears all data from the database (just like the /clear API), and then loads the
 * posted user, person, and event data into the database.
 */
public class LoadService extends Service{
    /**
     * Calls the clear service and then loads the data with the parent classes methods (getAllUsers, getAllPersons, getAllEvents)
     * @param myRequest The info for the load request
     * @return Returns a response form body for loading service
     */
    public LoadResult load(LoadRequest myRequest){
        LoadResult result = new LoadResult();
        int numUsersAdded = 0;
        int numPersonsAdded = 0;
        int numEventsAdded = 0;
        User[] users = myRequest.getUsers();
        Person[] persons = myRequest.getPersons();
        Event[] events = myRequest.getEvents();

        Connection conn = null;
        Database db = new Database();
        try {
            conn = db.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setMessage("Internal server error");
            return result;
        }
        //Clear all the tables
        try {
            db.clearTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        UserDao userDao = new UserDao(conn);
        PersonDao personDao = new PersonDao(conn);
        EventDao eventDao = new EventDao(conn);

        for (User user : users) {
            try {
                System.out.println("Loading user: " + user.getFirstName());
                userDao.insert(user);
                numUsersAdded += 1;
            } catch (DataAccessException e) {
                e.printStackTrace();
                result.setMessage("Internal server error");
                return result;
            }
        }
        for (Person person : persons) {
            try {
                System.out.println("Loading person: " + person.getFirstName());
                personDao.insert(person);
                numPersonsAdded += 1;
            } catch (DataAccessException e) {
                e.printStackTrace();
                result.setMessage("Internal server error");
                return result;
            }
        }
        for (Event event : events) {
            try {
                System.out.println("Loading event: " + event.getEventType());
                eventDao.insert(event);
                numEventsAdded += 1;
            } catch (DataAccessException e) {
                e.printStackTrace();
                result.setMessage("Internal server error");
                return result;
            }
        }

        try {
            db.closeConnection(true);
        } catch (SQLException e) {
            e.printStackTrace();
            result.setMessage("Internal server error");
            return result;
        }

        result.setMessage("Successfully added " + numUsersAdded + " users, " + numPersonsAdded +
                " persons, and " + numEventsAdded + " events to the database.");
        return result;
    }
}
