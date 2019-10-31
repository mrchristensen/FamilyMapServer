package dataaccess;

import exceptions.DataAccessException;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all the database operations for users
 */
public class UserDao extends Dao {
    private final Connection conn;

    public UserDao(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * Adds a user to the database
     */
    public void insert(User myUser) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (userName, password, email, firstName, lastName, gender, " +
                "personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, myUser.getUserName());
            stmt.setString(2, myUser.getPassword());
            stmt.setString(3, myUser.getEmail());
            stmt.setString(4, myUser.getFirstName());
            stmt.setString(5, myUser.getLastName());
            stmt.setString(6, myUser.getGender());
            stmt.setString(7, myUser.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in DAO - UserDAO");
            e.printStackTrace();
            throw new DataAccessException("Error: Username already taken by another user");
        }
    }

    /**
     * Retrieves a User object from the database
     * @param userName The username of the desired user
     * @return User object with that username
     */
    public User get(String userName) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("password"), rs.getString("email"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error in DAO - UserDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

//    /**
//     * Removes all users from the database
//     */
//    void removeAll() throws DataAccessException {
//        String sql = "DELETE FROM Users;";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DataAccessException("Error encountered while finding event");
//        }
//    }

//    void dropTable() throws DataAccessException {
//        String sql = "DROP TABLE Users;";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DataAccessException("Error encountered while finding event");
//        }
//    }
}
