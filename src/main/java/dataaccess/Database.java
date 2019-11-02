package dataaccess;

import exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the database connections and tables
 */
public class Database {
    private Connection conn;

    /**
     * Open a connection to the database to initiate transactions
     * @return A valid, open, connection
     * @throws DataAccessException Any DAO errors
     */
    public Connection openConnection() throws DataAccessException {
        //The path to the database (Structure for this Connection is driver:language:path)
        final String CONNECTION_URL = "jdbc:sqlite:database/familyMap.sqlite";

        // Open a database connection to the file given in the path
        try {
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Error in DAO - Database-DAO");
            e.printStackTrace();
            throw new DataAccessException("Error opening the connection");
        }

        return conn;
    }

    /**
     * Opens a new connection if there isn't one, else returns the current connection (singleton)
     * @return An open connection
     * @throws DataAccessException Any DAO errors
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /**
     *Closes the connection to the database, choosing to commit it or not
     *When we are done manipulating the database it is important to close the connection. This will
     *End the transaction and allow us to either commit our changes to the database or rollback any
     *changes that were made before we encountered a potential error.
     *
     *IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
     *DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
     *OR PROBLEMS YOU ENCOUNTER
     * @param commit Commit or rollback the transaction
     * @throws DataAccessException Any DAO errors
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) { //This will commit the changes to the database
                conn.commit();
            } else { //This will rollback any changes made during this connection's lifetime
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.out.println("Error in DAO - Database-DAO");
            e.printStackTrace();
            throw new DataAccessException("Error: Unable to close database connection");
        }
    }

    /**
     * Creates the server's tables if they don't already exist (Events, Users, Persons, and Authorization Tokens)
     * @throws DataAccessException Any DAO errors
     */
    public void createTables() throws DataAccessException {

        try (Statement stmt = conn.createStatement()){

            String sqlEventTable = "CREATE TABLE IF NOT EXISTS Events " +
                    "(" +
                    "EventID text not null unique, " +
                    "AssociatedUsername text not null, " +
                    "PersonID text not null, " +
                    "Latitude float not null, " +
                    "Longitude float not null, " +
                    "Country text not null, " +
                    "City text not null, " +
                    "EventType text not null, " +
                    "Year int not null, " +
                    "primary key (EventID), " +
                    "foreign key (AssociatedUsername) references Users(Username), " +
                    "foreign key (PersonID) references Persons(ID)" +
                    ")";
            stmt.executeUpdate(sqlEventTable);

            String sqlUserTable = "CREATE TABLE if not exists Users" +
                    "(" +
                    "userName TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "firstName TEXT NOT NULL," +
                    "lastName TEXT NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "personID TEXT NOT NULL," +
                    "PRIMARY KEY(userName)" +
                    ")";
            stmt.executeUpdate(sqlUserTable);

            String sqlPersonTable = "CREATE TABLE if not exists Persons" +
                    "(" +
                    "ID TEXT NOT NULL UNIQUE," +
                    "associatedUsername TEXT NOT NULL," +
                    "firstName TEXT NOT NULL," +
                    "lastName TEXT NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "fatherID TEXT," +
                    "motherID TEXT," +
                    "spouseID TEXT," +
                    "FOREIGN KEY(associatedUsername) REFERENCES Users(userName)," +
                    "FOREIGN KEY(fatherID) REFERENCES Persons(ID)," +
                    "FOREIGN KEY(motherID) REFERENCES Persons(ID)," +
                    "PRIMARY KEY(ID)" +
                    ")";
            stmt.executeUpdate(sqlPersonTable);

            String sqlAuthTokenTable = "CREATE TABLE if not exists AuthorizationTokens" +
                    "(" +
                    "userName	TEXT NOT NULL," +
                    "authToken	TEXT NOT NULL," +
                    "FOREIGN KEY(userName) REFERENCES Users(userName)," +
                    "PRIMARY KEY(authToken)" +
                    ")";
            stmt.executeUpdate(sqlAuthTokenTable);
        } catch (SQLException e) {
            System.out.println("Error in DAO - Database-DAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while creating tables");
        }
    }

    /**
     * Clears all the tables of the sql database
     * @return Returns 0 on success
     */
    public int clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM Users";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM authorizationTokens";
            stmt.executeUpdate(sql);

            return 0;

        } catch (SQLException e) {
            System.out.println("Error in DAO - Database-DAO");
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables - clear failed");
        }
    }
}

