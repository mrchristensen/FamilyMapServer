package service;

import dataaccess.Database;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.RegisterRequest;
import result.Result;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
class LoadServiceTest {
    private Database db;
    private ClearService cs;

    @BeforeEach
    void setUp() throws Exception {
        db = new Database();
        cs = new ClearService();
        db.openConnection();
        db.createTables(); //Ensure that the tables are created
        db.closeConnection(true);

    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    void fillGenerationsPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);

        Result result = new LoadService().load(registerRequest.getUsername(), 1);

        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().contains("Error"));
    }

    @Test
    void fillGenerationsFail() {
        User[] users = null;
        Person[] persons = null;
        Event[] events = null;

        LoadRequest loadRequest = new LoadRequest(users, persons, events);
        Result result = new LoadService().load(loadRequest);

        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().contains("Error"));
    }

}
