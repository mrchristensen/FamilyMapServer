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
        String sql = "INSERT INTO Users (userName, password, email, firstName, lastName, gender, " +
                "personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

}
