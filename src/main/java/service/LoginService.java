package service;

import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import request.*;
import result.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Handles the login functionality - Logs in the user and returns an auth token
 */
public class LoginService extends Service {
    /**
     * Logs-in a user - get's auth token of the user's session
     * @param myRequest The login request info
     * @return Response body of the login api call
     */
    public LoginResult loginUser(LoginRequest myRequest) {
        try {
            LoginResult result = new LoginResult();


            //Make sure user exists
            Database db = new Database();
            Connection conn = db.getConnection();
            UserDao userDao = new UserDao(conn);
            User user;
            user = userDao.get(myRequest.getUsername());
            if (user == null) {
                System.out.println("Error in finding the user (no such user exists): " + myRequest.getUsername());
                result.setMessage("Error: User does not exist");
                db.closeConnection(false);
                return result;
            }

            //Make sure the password matches the user
            if (!myRequest.getPassword().equals(user.getPassword())) {
                System.out.println("Password is incorrect: " + myRequest.getPassword() + " != " + user.getPassword());
                result.setMessage("Error: Password is incorrect");
                db.closeConnection(false);
                return result;
            }

            //Insert the auth token into the authToken table
            AuthToken authToken = new AuthToken(myRequest.getUsername(), UUID.randomUUID().toString());
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            authTokenDao.insert(authToken);


            //If we got to this point is was a success
            result.setAuthToken(authToken.getAuthTokenString());
            result.setUserName(myRequest.getUsername());
            result.setPersonID(user.getPersonID());


            db.closeConnection(true);


            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new LoginResult(e.toString());
        }
    }
}
