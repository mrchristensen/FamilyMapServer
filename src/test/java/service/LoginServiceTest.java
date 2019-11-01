package service;

import dataaccess.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.Result;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
class LoginServiceTest {
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
    void loginUserPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Result result = new LoginService().loginUser(loginRequest);

        assertNull(result.getMessage());
    }

    @Test
    void loginUserFail() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                "firstName", "lastName", "f");
        new RegisterService().registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("invalid-username", "wrong-password");
        Result result = new LoginService().loginUser(loginRequest);

        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().contains("Error"));
    }

}
