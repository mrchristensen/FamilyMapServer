package dataaccess;

import exceptions.DataAccessException;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class DatabaseTest {
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
//        db.openConnection();
//        db.createTables();
//        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
//        db.openConnection();
//        db.clearTables();
//        db.closeConnection(true);
    }

    @Test
    public void openConnectionPass() throws Exception {
        Connection conn = null;

        try {
            conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }
        assertNotNull(conn);
    }

    @Test
    public void openConnectionFail() throws Exception {
        boolean error = false;
        Connection conn = null;

        try {
            db = null;
            conn = db.openConnection(); //openConnection on a null server
        } catch (DataAccessException | NullPointerException e) {
            error = true;
            System.out.println(e.getMessage());
        }

        assertNull(conn);
        assertTrue(error);
    }

    @Test
    public void getConnectionPass() throws Exception {
        Connection conn = null;
        Connection conn2 = null;

        try {
            conn = db.openConnection();
            conn2 = db.getConnection();
            db.closeConnection(false);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }
        assertNotNull(conn);
        assertNotNull(conn2);
        assertEquals(conn, conn2);
    }

    @Test
    public void getConnectionFail() throws Exception {
        boolean error = false;
        Connection conn = null;

        try {
            db = null;
            conn = db.getConnection(); //openConnection on a null server
        } catch (DataAccessException | NullPointerException e) {
            error = true;
            System.out.println(e.getMessage());
        }

        assertNull(conn);
        assertTrue(error);
    }

    @Test
    public void closeConnectionPass() throws Exception {
        Connection conn = null;

        try {
            conn = db.openConnection();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }
        assertTrue(conn.isClosed());
    }

    @Test
    public void closeConnectionFail() throws Exception {
        boolean error = false;
        Connection conn = null;

        try {
            db.closeConnection(true); //openConnection on a null server
        } catch (DataAccessException | NullPointerException e) {
            error = true;
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }

    @Test
    public void createTablesPass() throws Exception {
        Connection conn = null;
        Event compareTest = null;
        try {
            conn = db.openConnection();
            db.createTables();
            EventDao eDoa = new EventDao(conn);
            eDoa.insert(bestEvent);
            compareTest = eDoa.get(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }
        assertNotNull(conn);
        assertNotNull(compareTest); //Thus tables have to exist
    }

    @Test
    public void createTablesFail() throws Exception {
        boolean error = false;

        try {
//            db = null;
            db.createTables(); //openConnection on a null server
        } catch (DataAccessException | NullPointerException e) {
            error = true;
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }

    @Test
    public void clearTablesPass() throws Exception {
        Connection conn;
        Event compareTest1 = null;

        try {
            conn = db.openConnection();
            db.createTables();
            EventDao eDoa = new EventDao(conn);
            eDoa.insert(bestEvent);
            compareTest1 = eDoa.get(bestEvent.getEventID());
            db.clearTables();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }
        assertNull(compareTest1);
    }

    @Test
    public void clearTablesFail() throws Exception {
        boolean error = false;

        try {
            db.clearTables(); //openConnection on a null server
        } catch (DataAccessException | NullPointerException e) {
            error = true;
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }


}
