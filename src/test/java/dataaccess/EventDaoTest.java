package dataaccess;

import exceptions.DataAccessException;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private Event nextBestEvent;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        nextBestEvent = new Event("Biking_123B", "Gale", "Bob",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //and make sure to initialize our tables since we don't know if our database files exist yet
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.insert(bestEvent);
            //So lets use a find method to get the event that we just put in back out
            compareTest = eDao.get(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //if we call the method the first time it will insert it successfully
            eDao.insert(bestEvent);
            //but our sql table is set up so that "eventID" must be unique. So trying to insert it
            //again will cause the method to throw an exception
            eDao.insert(bestEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of insert should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        Event compareTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = eDao.get(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void getPass() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.get(bestEvent.getEventID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(bestEvent, compareTest); //Make sure get returns the same person that we originally had

    }

    @Test
    public void getFail() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            compareTest = eDao.get(bestEvent.getEventID()); //Get an event that hasn't been added

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertNull(compareTest); //Make sure that null is return as you cannot get a event doesn't exist
    }

    @Test
    public void getSpecificPass() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.get(bestEvent.getPersonID(), bestEvent.getEventType());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(bestEvent, compareTest); //Make sure get returns the same person that we originally had

    }

    @Test
    public void getSpecificFail() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            compareTest = eDao.get(bestEvent.getPersonID(), bestEvent.getEventType()); //Get an event that hasn't been added

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertNull(compareTest); //Make sure that null is return as you cannot get a event doesn't exist
    }

    @Test
    public void getAllPass() throws Exception {
        Event[] compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            eDao.insert(bestEvent);
            compareTest = eDao.getAll(bestEvent.getAssociatedUsername());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assert compareTest != null;
        assertEquals(bestEvent, compareTest[0]); //Make sure get returns the same event that we originally had

    }

    @Test
    public void getAllFail() throws Exception {
        Event compareTest[] = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            compareTest = eDao.getAll(bestEvent.getAssociatedUsername()); //Get event that hasn't been added
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(compareTest.length, 0); //Make sure that null is return as you cannot get a event doesn't exist
    }

    @Test
    public void removePass() throws Exception {
        Event compareTest1 = null;
        Event compareTest2 = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            eDao.insert(bestEvent);
            eDao.remove(bestEvent.getEventID());
            compareTest1 = eDao.get(bestEvent.getEventID()); //Should be null (because we removed the person)
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        assertNull(compareTest1);
    }

    @Test
    public void removeFail() throws Exception {
        boolean error = false;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.dropTable(); //Drop the table and then try to remove all entries (in a non-existent table)
            eDao.remove(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) { //For trying to clear a non-existent table)
            error = true;
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }

    @Test
    public void removeUsersRelativesEventsPass() throws Exception {
        Event compareTest1 = null;
        Event compareTest2 = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            eDao.insert(bestEvent);
            eDao.insert(nextBestEvent);
            eDao.removeUsersRelativesEvents(bestEvent.getAssociatedUsername(), bestEvent.getPersonID());

            compareTest1 = eDao.get(bestEvent.getEventID()); //Should be null (because we removed the person)
            compareTest2 = eDao.get(nextBestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        assertEquals(compareTest1, bestEvent);
        assertNull(compareTest2);
    }

    @Test
    public void removeUsersRelativesEventsFail() throws Exception {
        Event compareTest = null;
        boolean error = false;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);

            eDao.dropTable(); //Drop the table and then try to remove all entries (in a non-existent table)
            eDao.removeUsersRelativesEvents(bestEvent.getAssociatedUsername(), bestEvent.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException e) { //For trying to clear a non-existent table)
            error = true;
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }
}
