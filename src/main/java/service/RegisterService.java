package service;

import dataaccess.Database;
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
        Person usersPerson = createUserPerson(myRequest);

        new Generation().genGenerations(usersPerson, 4); //Generate data for the new user (also add user's person to the data base)

        //Adds user to the user database table
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao userDao = new UserDao(conn);
        User user = createUser(myRequest, usersPerson.getPersonID());
        try {
            userDao.insert(user);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
        db.closeConnection(true);

        RegisterResult result = new RegisterResult();

        //Logs in the user
        AuthToken authToken = login(myRequest);
        //Todo: check for an error and return an error message (with a catch block)

        result.setAuthToken(authToken);
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

    AuthToken login(RegisterRequest myRequest){
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername(myRequest.getUsername());
        loginRequest.setPassword(myRequest.getPassword());

        LoginResult loginResult= new LoginService().loginUser(loginRequest);

        return loginResult.getAuthToken();
    }


}
