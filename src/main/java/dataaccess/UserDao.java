package dataaccess;

import model.User;

/**
 * Handles all the database operations for users
 */
public class UserDao extends Dao {

    /**
     * Adds a user to the database
     * @param userName the username of the new current user
     * @param password the password of the new current user
     * @param email the email of the new current user
     * @param firstName the first name of the new current user
     * @param lastName the last name of the new current user
     * @param gender the gender of the new current user ("f" or "s")
     */
    void insertUser(String userName, String password, String email, String firstName, String lastName, String gender){
        //Todo: Check to see if the username is already taken and handle the delete and other stuff (watch for this case)
    }

    /**
     * Retrieves a User object from the database
     * @param userName The username of the desired user
     * @return User object with that username
     */
    User getUser(String userName){
        return null;
    }

//    /**
//     * Finds all users in the database
//     * @return Returns an array of all User objects
//     */
//    User[] getAllUsers(){
//        return null;
//    }

//    /**
//     * Remove a user from the database
//     * @param username The username of the user to remove from the database
//     */
//    void removeUser(String username){
//    }

    /**
     * Removes all users from the database
     */
    void removeAllUsers(){
    }



}
