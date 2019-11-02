package service;

import dataaccess.Database;
import dataaccess.EventDao;
import dataaccess.PersonDao;
import dataaccess.UserDao;
import exceptions.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;
import result.Result;

import java.sql.Connection;

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
    public Result load(LoadRequest myRequest){
        try {
            LoadResult result = new LoadResult();
            int numUsersAdded = 0;
            int numPersonsAdded = 0;
            int numEventsAdded = 0;
            User[] users = myRequest.getUsers();
            Person[] persons = myRequest.getPersons();
            Event[] events = myRequest.getEvents();

            Connection conn;
            Database db = new Database();
            conn = db.getConnection();
            //Clear all the tables

            db.clearTables();

            UserDao userDao = new UserDao(conn);
            PersonDao personDao = new PersonDao(conn);
            EventDao eventDao = new EventDao(conn);

            for (User user : users) {

                System.out.println("Loading user: " + user.getFirstName());
                userDao.insert(user);
                numUsersAdded += 1;

            }
            for (Person person : persons) {

                System.out.println("Loading person: " + person.getFirstName());
                personDao.insert(person);
                numPersonsAdded += 1;

            }
            for (Event event : events) {

                System.out.println("Loading event: " + event.getEventType());
                eventDao.insert(event);
                numEventsAdded += 1;

            }

            db.closeConnection(true);

            result.setMessage("Successfully added " + numUsersAdded + " users, " + numPersonsAdded +
                    " persons, and " + numEventsAdded + " events to the database.");
            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Result(e.toString());
        }
    }
}
