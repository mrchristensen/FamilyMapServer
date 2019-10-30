package dataaccess;

import exceptions.DataAccessException;
import model.AuthToken;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations for events
 */
public class EventDao extends Dao {
    private final Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Add a event into the database
     * @param myEvent The event to insert into the database
     */
    public void insert(Event myEvent) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, myEvent.getEventID());
            stmt.setString(2, myEvent.getAssociatedUsername());
            stmt.setString(3, myEvent.getPersonID());
            stmt.setDouble(4, myEvent.getLatitude());
            stmt.setDouble(5, myEvent.getLongitude());
            stmt.setString(6, myEvent.getCountry());
            stmt.setString(7, myEvent.getCity());
            stmt.setString(8, myEvent.getEventType());
            stmt.setInt(9, myEvent.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Delete an event from the database
     * @param eventID The id of the event to remove
     */
    void remove(String eventID){
    }

    /**
     * Deletes all events from the database
     */
    void removeAll(){

    }

    /**
     * Retrieve a single event from the database
     * @param eventID The ID of the desired event
     * @return The event object of the desired event
     */
    public Event get(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
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

    public Event[] getAll(String associatedUserName) throws DataAccessException {
        System.out.println("Get all events that belong to the user");
        List<Event> events = new ArrayList<>();
        Event event = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUserName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                System.out.println("Found an event that is associated with the user: " + event.getEventType());
                events.add(event);
            }
            return events.toArray(new Event[0]);
        } catch (SQLException e) {
            System.out.println("Error encountered while finding event");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                }
            }

        }

    }

    public Event get(String personID, String eventType) throws DataAccessException {
        System.out.println("Get event with a userName and eventType");
        Event event = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Events WHERE PersonID = ? AND EventType = ?;";
//        String sql = "SELECT * FROM Events WHERE AssociatedUsername = " + associatedUser + " AND EventType = " + eventType + ";";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.setString(2, eventType);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                System.out.println("Found the correct event (of type): " + event.getEventType());
                return event;
            }
        } catch (SQLException e) {
            System.out.println("Error encountered while finding event");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    /**
     * Retrieves all events of all family of the current user (from the database)
     * @param userAuthToken The auth token of the current user
     * @return Array of all events in the database (of all family members of user)
     */
    Event[] getAllAncestralEvents(AuthToken userAuthToken){
        return null;
    }

}
