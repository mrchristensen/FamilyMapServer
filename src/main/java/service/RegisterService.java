package service;

import dataaccess.Database;
import dataaccess.EventDao;
import dataaccess.UserDao;
import exceptions.DataAccessException;
import generation.Generation;
import model.*;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Handles the register functionality - Creates a new user account, generates 4 generations of ancestor data for the new
 * user, logs the user in, and returns an auth token.
 */
public class RegisterService extends Service {
    /**
     *
     * @param myRequest Request body for register api call
     * @return RegisterService
     */
    public RegisterResult registerUser(RegisterRequest myRequest) throws SQLException, IOException, DataAccessException {
        RegisterResult result = new RegisterResult();
        Person usersPerson = createUserPerson(myRequest);

        //Adds user to the user database table
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao userDao = new UserDao(conn);
        User user = createUser(myRequest, usersPerson.getPersonID());
        try {
            userDao.insert(user);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println("That username is already taken");
            result.setMessage("Username already taken by another user");
            return result;
        }
        db.closeConnection(true);

        //Create and insert a "Birth" event for the user (to help for generation)
        createBirthEvent(usersPerson);

        //Generate data for the new user (also add user's person to the data base)
        new Generation().genGenerations(usersPerson, 4);

        //Logs in the user
        String authToken = null;
        try {
            authToken = login(myRequest);
        } catch (SQLException e) {
            System.out.println("Error on logging in the new user");
            e.printStackTrace();
            result.setMessage("Internal server error - Login failed");
            return result;
        }
        //Todo: check for an error and return an error message (with a catch block)

        result.setAuthTokenString(authToken);
        result.setUserName(myRequest.getUsername());
        result.setPersonID(usersPerson.getPersonID());

        return result;
    }

    /**
     * Helper function to create the new user object
     */
    User createUser(RegisterRequest myRequest, String personID){
        String userName = myRequest.getUsername();
        String password = myRequest.getPassword();
        String email = myRequest.getEmail();
        String firstName = myRequest.getFirstName();
        String lastName = myRequest.getLastName();
        String gender = myRequest.getGender();

        return new User(userName, password, email, firstName, lastName, gender, personID);
    }

    /**
     * Helper function to create the new user's person object
     */
    Person createUserPerson(RegisterRequest myRequest){
        String personID = UUID.randomUUID().toString();
        String associatedUsername = myRequest.getUsername();
        String firstName = myRequest.getFirstName();
        String lastName = myRequest.getLastName();
        String gender = myRequest.getGender();

        return new Person(personID, associatedUsername, firstName, lastName, gender);
    }

    void createBirthEvent(Person person) throws SQLException {
        Event myEvent = new Generation().genEvent(person, "Birth", 1997);

        Database db = new Database();
        Connection conn = db.openConnection();
        EventDao eventDao = new EventDao(conn);

        try {
            eventDao.insert(myEvent);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }

        db.closeConnection(true);
    }

    String login(RegisterRequest myRequest) throws SQLException {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername(myRequest.getUsername());
        loginRequest.setPassword(myRequest.getPassword());

        LoginResult loginResult= new LoginService().loginUser(loginRequest);

        return loginResult.getAuthTokenString();
    }


}
