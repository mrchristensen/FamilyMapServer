package service;

import dataaccess.Database;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.Result;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//We will use this to test that our insert method is working and failing in the right ways
class LoadServiceTest {
    private Database db;
    private ClearService cs;
    private Person bestPerson;
    private Event bestEvent;
    private User bestUser;

    @BeforeEach
    void setUp() throws Exception {
        db = new Database();
        cs = new ClearService();
        db.openConnection();
        db.createTables(); //Ensure that the tables are created
        db.closeConnection(true);
        bestPerson = new Person("personID1", "associatedUsername", "firstName",
                "lastName", "f", "fatherID", "motherID", "spouseID");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestUser = new User("userName", "password", "email", "firstName",
                "lastName", "f", "personID");
    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    void loadPass() {
        List<Event> events = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<User> users = new ArrayList<>();
        events.add(bestEvent);
        users.add(bestUser);
        persons.add(bestPerson);

        LoadRequest loadRequest = new LoadRequest(users.toArray(new User[0]), persons.toArray(new Person[0]), events.toArray(new Event[0]));

        Result result = new LoadService().load(loadRequest);

        assertNotNull(result.getMessage());  //Guarantees no error
    }

    @Test
    void loadFail() {
        List<Event> events = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<User> users = new ArrayList<>();

        LoadRequest loadRequest = new LoadRequest(users.toArray(new User[0]), persons.toArray(new Person[0]), events.toArray(new Event[0]));

        Result result = new LoadService().load(loadRequest);

        assertNotNull(result.getMessage());  //Guarantees no error
        assertTrue(result.getMessage().contains("added 0"));
    }

}
