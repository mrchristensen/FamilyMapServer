package service;

import dataaccess.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
class RegisterServiceTest {
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
    void registerUserPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        RegisterResult result = new RegisterService().registerUser(registerRequest);


        assertNull(result.getMessage());
    }

    @Test
    void registerUserFail() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);
        RegisterResult result = new RegisterService().registerUser(registerRequest);

        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().contains("Error"));
    }

}
