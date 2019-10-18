package service;

import model.AuthToken;
import request.RegisterRequest;
import result.RegisterResult;

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
    RegisterResult registerUser(RegisterRequest myRequest){
        return null;
    }
//
//    /**
//     * Creates a new user
//     * @param username the username of the current user
//     * @param password the password of the current user
//     * @param email the email of the current user
//     * @param firstName the first name of the current user
//     * @param lastName the last name of the current user
//     * @param gender the gender of the current user
//     */
//    void registerUser(String username, String password, String email, String firstName, String lastName, String gender){
//    }

    /**
     * Helper function to create the new user account
     */
    void createUserAccount(){
    }

    /**
     * Generates 4 generations of ancestor data for a new user
     */
    void generateAncestralData(){
    }

    /**
     * Logs-in in user
     * @return Auth Token of the newly logged-in user
     */
    AuthToken login(){
        return null;
    }


}
