package dataaccess;

import exceptions.DataAccessException;
import model.AuthToken;
import model.Event;
import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all database operations for persons
 */
public class PersonDao extends Dao {
    private final Connection conn;

    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Adds a new person to the database
     */
    void insert(Person myPerson) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (ID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, myPerson.getPersonID());
            stmt.setString(2, myPerson.getAssociatedUsername());
            stmt.setString(3, myPerson.getFirstName());
            stmt.setString(4, myPerson.getLastName());
            stmt.setString(5, myPerson.getGender());
            stmt.setString(6, myPerson.getFatherID());
            stmt.setString(7, myPerson.getMotherID());
            stmt.setString(8, myPerson.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

//    /**
//     * Delete an person from the database
//     * @param personID The ID of the person to remove
//     */
//    void removePerson(String personID){
//    }

    /**
     * Deletes all persons from the database
     */
    void removeAll() throws DataAccessException {


        String sql = "DELETE FROM Persons;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }

    }

    void dropTable() throws DataAccessException {
        String sql = "DROP TABLE Persons;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }

    }

    /**
     * Retrieve a single person from the database
     * @param personID The ID of the desired person
     * @return The person object of the desired person
     */
    Person get(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE ID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("ID"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
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
//     * Retrieves all persons of all family of the current user (from the database)
//     * @param userAuthToken The auth token of the current user
//     * @return Array of all persons in the database (of all family members of user)
//     */
//    Person[] getAllAncestralPersons(AuthToken userAuthToken){
//        return null;
//    }

}
