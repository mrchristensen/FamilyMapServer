package dataaccess;

import exceptions.DataAccessException;
import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all database operations for persons
 */
public class AuthTokenDao extends Dao {
    private final Connection conn;

    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Handles a new session for a given user
     * @param userName the username of the user logging-in
     * @return the auth token of this session (tied to the user)
     */
    AuthToken startSession(String userName){

        return null;
    }

    /**
     * Insert an authToken into the database
     * @param myAuthToken The authToken to insert into the database
     */
    public void insert(AuthToken myAuthToken) throws DataAccessException {
        String sql = "INSERT INTO authorizationTokens (userName, authToken) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myAuthToken.getUserName());
            stmt.setString(2, myAuthToken.getAuthTokenString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in DAO - AuthTokenDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
    }

//    /**
//     * Deletes all auth tokens from the database
//     */
//    void removeAll() {
//        String sql = "DELETE FROM AuthorizationTokens;";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.executeQuery();
//        } catch (SQLException e) {
//            throw new DataAccessException("Error encountered while finding person")
//        }
//    }

//    /**
//     *Get the authToken of a given user's current session
//     * @param userName The username of the user
//     * @return The authToken of a given user's current session
//     */
//    AuthToken getAuthToken(String userName) throws DataAccessException {
//        AuthToken authToken;
//        ResultSet rs = null;
//        String sql = "SELECT * FROM AuthorizationTokens WHERE userName = ?;";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, userName);
//            rs = stmt.executeQuery();
//            if (rs.next()) {
//                authToken = new AuthToken(rs.getString("userName"), rs.getString("authToken"));
//                return authToken;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error in DAO - AuthTokenDAO");
//            e.printStackTrace();
//            throw new DataAccessException("Error encountered while finding person");
//        } finally {
//            if(rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return null;
//    }

    public String getAuthUsername(String authToken) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthorizationTokens WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                AuthToken token = new AuthToken(rs.getString("userName"), rs.getString("authToken"));
                return rs.getString("userName");
            }
        } catch (SQLException e) {
            System.out.println("Error in DAO - AuthTokenDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
