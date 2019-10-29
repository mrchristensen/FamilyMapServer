package service;

import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;

/**
 * Handles the login functionality - Logs in the user and returns an auth token
 */
public class LoginService extends Service {
    /**
     * Logs-in a user - get's auth token of the user's session
     * @param myRequest The login request info
     * @return Response body of the login api call
     */
    public LoginResult loginUser(LoginRequest myRequest){
        return null;
    }
}
