package dataaccess;

import exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:database/familyMap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        try (Statement stmt = conn.createStatement()){
            //First lets open our connection to our database.

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exactly like a sql create table command
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
                    "foreign key (PersonID) references Persons(PersonID)" +
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
                    "ID INTEGER NOT NULL," +
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

            String sqlAuthTokenTable = "CREATE TABLE if not exists authorizationTokens" +
                    "(" +
                    "userName	TEXT NOT NULL," +
                    "authToken	TEXT NOT NULL," +
                    "FOREIGN KEY(userName) REFERENCES Users(userName)," +
                    "PRIMARY KEY(authToken)" +
                    ")";

            stmt.executeUpdate(sqlAuthTokenTable);

            //if we got here without any problems we successfully created the table and can commit
        } catch (SQLException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            throw new DataAccessException("SQL Error encountered while creating tables");
        }


    }

    public void clearTables() throws DataAccessException
    {

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}

