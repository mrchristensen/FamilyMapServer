package service;

import dataaccess.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.Result;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//We will use this to test that our insert method is working and failing in the right ways
class EventIDServiceTest {
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
    void retrieveEventPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);

        Result eventIDResponse = new EventIDService().retrieveEvent(registerRequest.getFirstName(), registerRequest.getUsername());

        assertNotNull(eventIDResponse.getMessage());

    }

    @Test
    void retrieveEventFail() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);

        Result eventIDResponse = new EventIDService().retrieveEvent("fake username", "fake id");

        assertTrue(eventIDResponse.getMessage().contains("Error"));
    }

}
