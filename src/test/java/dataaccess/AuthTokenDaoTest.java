package dataaccess;

import exceptions.DataAccessException;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthTokenDaoTest {
    private Database db;
    private AuthToken bestAuth;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestAuth = new AuthToken("username", "1234");

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
        String compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(bestAuth);
            compareTest = aDao.getAuthUsername(bestAuth.getAuthTokenString());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(bestAuth.getUserName(), compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao eDao = new AuthTokenDao(conn);
            eDao.insert(bestAuth);

            eDao.insert(bestAuth);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        String compareTest = bestAuth.getUserName();
        try {
            Connection conn = db.openConnection();
            AuthTokenDao eDao = new AuthTokenDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = eDao.getAuthUsername(bestAuth.getAuthTokenString());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void getAuthUsernamePass() throws Exception {
        String compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao eDao = new AuthTokenDao(conn);
            eDao.insert(bestAuth);
            compareTest = eDao.getAuthUsername(bestAuth.getAuthTokenString());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(bestAuth.getUserName(), compareTest);

    }

    @Test
    public void getFail() throws Exception {
        String compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao eDao = new AuthTokenDao(conn);

            compareTest = eDao.getAuthUsername(bestAuth.getAuthTokenString()); //Get an event that hasn't been added

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertNull(compareTest); //Make sure that null is return as you cannot get a auth token doesn't exist
    }

}
