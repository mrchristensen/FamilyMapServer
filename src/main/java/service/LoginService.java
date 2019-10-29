package service;

import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import request.*;
import result.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the login functionality - Logs in the user and returns an auth token
 */
public class LoginService extends Service {
    /**
     * Logs-in a user - get's auth token of the user's session
     * @param myRequest The login request info
     * @return Response body of the login api call
     */
    public LoginResult loginUser(LoginRequest myRequest) throws SQLException {
        LoginResult result = new LoginResult();


        //Make sure user exists
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao userDao = new UserDao(conn);
        User user = null;
        try {
            user = userDao.get(myRequest.getUsername());
        } catch (DataAccessException e) {
            db.closeConnection(false);
            System.out.println("Error in finding the user - internal server error");
            e.printStackTrace();
            result.setMessage("Internal server error");
            return result;
        }
        if(user == null){
            System.out.println("Error in finding the user (no such user exists)");
            result.setMessage("User does not exist");
            return result;
        }

        //Make sure the password matches the user
        if(myRequest.getPassword() != user.getPassword()){
            System.out.println("Password is incorrect");
            result.setMessage("Password is incorrect");
            return result;
        }

        //Insert the auth token into the authToken table
        AuthToken authToken = new AuthToken(myRequest.getUsername(), myRequest.getPassword());
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        try {
            authTokenDao.insert(authToken);
        } catch (SQLException e) {
            db.closeConnection(false);
            System.out.println("SQL Error while trying to insert the auth token upon login");
            e.printStackTrace();
            result.setMessage("Internal Server error - SQL Error on inserting auth token");
            return result;
        }

        //If we got to this point is was a success
        result.setAuthToken(authToken);
        result.setUserName(myRequest.getUsername());
        result.setPersonID(user.getPersonID());

        db.closeConnection(true);

        return result;
    }
}
