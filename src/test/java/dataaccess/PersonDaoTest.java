package dataaccess;

import exceptions.DataAccessException;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private Person nextBestPerson;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestPerson = new Person("personID1", "associatedUsername", "firstName", "lastName", "f", "fatherID", "motherID", "spouseID");
        nextBestPerson = new Person("personID2", "associatedUsername", "firstName", "lastName", "f", "fatherID", "motherID", "spouseID");

        db.openConnection();
        db.createTables(); //Ensure that the tables are created
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            compareTest = eDao.get(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        //Make sure that the insert actually put the person in the database
        assertNotNull(compareTest);

        //Make sure that the person we put in and got back in the same as the original person
        assertEquals(bestPerson, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        //Try inserting two of the same person
        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);
            eDao.insert(bestPerson);
            eDao.insert(bestPerson); //Insert the same person twice
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception it is because that person was already in the database
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

        //Make sure that the roll back (db.closeConnection(false);) didn't store the person in the data base
        Person compareTest = bestPerson;
        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);
            compareTest = eDao.get(bestPerson.getPersonID()); //Should be null
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void getPass() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            compareTest = eDao.get(bestPerson.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(bestPerson, compareTest); //Make sure get returns the same person that we originally had

    }

    @Test
    public void getFail() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            compareTest = eDao.get(bestPerson.getPersonID()); //Get someone that hasn't been added

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertNull(compareTest); //Make sure that null is return as you cannot get a person doesn't exist
    }

    @Test
    public void getAllPass() throws Exception {
        Person[] compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            compareTest = eDao.getAll(bestPerson.getAssociatedUsername());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assert compareTest != null;
        assertEquals(bestPerson, compareTest[0]); //Make sure get returns the same person that we originally had

    }

    @Test
    public void getAllFail() throws Exception {
        Person compareTest[] = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            compareTest = eDao.getAll(bestPerson.getAssociatedUsername()); //Get someone that hasn't been added

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertEquals(compareTest.length, 0); //Make sure that null is return as you cannot get a person doesn't exist
    }

    @Test
    public void removeUsersRelativesPass() throws Exception {
        Person compareTest1 = null;
        Person compareTest2 = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            eDao.insert(nextBestPerson);
            eDao.removeUsersRelatives(bestPerson.getAssociatedUsername(), bestPerson.getPersonID());

            compareTest1 = eDao.get(bestPerson.getPersonID()); //Should be null (because we removed the person)
            compareTest2 = eDao.get(nextBestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        assertEquals(compareTest1, bestPerson);
        assertNull(compareTest2);
    }

    @Test
    public void removeUsersRelativesFail() throws Exception {
        Person compareTest = null;
        boolean error = false;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.dropTable(); //Drop the table and then try to remove all entries (in a non-existent table)
            eDao.removeUsersRelatives(bestPerson.getAssociatedUsername(), bestPerson.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException e) { //For trying to clear a non-existent table)
            error = true;
            db.closeConnection(false);
            System.out.println(e.getMessage());
        }

        assertTrue(error);
    }

}
