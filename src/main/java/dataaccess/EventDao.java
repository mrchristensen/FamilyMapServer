package dataaccess;

import exceptions.DataAccessException;
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
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myEvent.getEventID());
            stmt.setString(2, myEvent.getAssociatedUsername());
            stmt.setString(3, myEvent.getPersonID());
            stmt.setFloat(4, myEvent.getLatitude());
            stmt.setFloat(5, myEvent.getLongitude());
            stmt.setString(6, myEvent.getCountry());
            stmt.setString(7, myEvent.getCity());
            stmt.setString(8, myEvent.getEventType());
            stmt.setInt(9, myEvent.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
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
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error in DAO - EventDAO");
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Retrieve a single event from the database without knowing the event's ID
     * @param personID The PersonID of the desired event
     * @param eventType The ID of the desired event
     * @return The event object of the desired event
     * @throws DataAccessException Any errors in retrieving the event
     */
    public Event get(String personID, String eventType) throws DataAccessException {
        System.out.println("Get event with a userName and eventType: " + personID + " " + eventType);
        Event event;
        ResultSet rs = null;

        String sql = "SELECT * FROM Events WHERE PersonID = ? AND EventType = ?;";
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
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error!");
                    System.out.println("Error in DAO - EventDAO");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Get all the events that belong to the user
     * @param associatedUserName The username of the user
     * @return An array of all the events that belong to the current user
     * @throws DataAccessException And errors in getting the related events
     */
    public Event[] getAll(String associatedUserName) throws DataAccessException {
        System.out.println("Get all events that belong to the user");
        List<Event> events = new ArrayList<>();
        Event event;
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
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error in DAO - EventDAO");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Remove a single event from the database, by eventID
     * @param eventID The eventID of the event to remove
     * @throws DataAccessException Any error in the process of deleting the event
     */
    public void remove(String eventID) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error in removing a single event");
        }
    }

    /**
     * Removes all the events of the user's relatives
     * @param username Username of the user
     * @param personID PersonID of the user's person object
     * @throws DataAccessException Any error in the process of removing the relatives events
     */
    public void removeUsersRelativesEvents(String username, String personID) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE associatedUsername = ? AND PersonID != ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in DAO - EventDAO");
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
    }

    /**
     * Drops the Events table
     * @throws DataAccessException Error in dropping table
     */
    void dropTable() throws DataAccessException {
        String sql = "DROP TABLE Events;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }

    }

}
