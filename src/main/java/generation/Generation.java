package generation;

import dataaccess.Database;
import dataaccess.EventDao;
import dataaccess.PersonDao;
import exceptions.DataAccessException;
import model.Event;
import model.Location;
import model.Person;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static handlers.JsonDeserialization.jsonToLocations;
import static handlers.JsonDeserialization.jsonToStrings;

public class Generation {
    private List<String> femaleNames;
    private List<String> maleNames;
    private List<String> surNames;
    private List<Location> locations;
    private int personsAdded;
    private int eventsAdded;
    private Random r = new Random();

    public Generation() {
        femaleNames = jsonToStrings(new File("json/fnames.json"));
        maleNames = jsonToStrings(new File("json/mnames.json"));
        surNames = jsonToStrings(new File("json/snames.json"));
        locations = jsonToLocations(new File("json/locations.json"));
        personsAdded = 0;
        eventsAdded = 0;
    }

    /**
     * Generate X generations of family history data for the initially passed in child (recursive)
     * @param child Who to give parents to
     * @param numGenerations How many generations
     */
    public void genGenerations(Person child, int numGenerations) throws DataAccessException {
        System.out.println("Generations left: " + numGenerations + "\nChild: " + child.getFirstName());

        Person dad = genPerson(child.getAssociatedUsername(), "m");
        Person mom = genPerson(child.getAssociatedUsername(), "f");
        mom.setSpouseID(dad.getPersonID());
        dad.setSpouseID(mom.getPersonID());
        child.setMotherID(mom.getPersonID());
        child.setFatherID(dad.getPersonID());

        int dateOfBirthMom = genBirthYear(child); //More than 13 before child's date of birth
        int dateOfBirthDad = genBirthYear(child);
        int dateOfMarriage = genMarriageYear(dateOfBirthMom, dateOfBirthDad); //At least 19 years after either's birthday
        Location locationOfMarriage = locations.get(r.nextInt(locations.size()));
        int dateOfDeathMom = genDeathYear(dateOfBirthMom); //After the birth of child, not more than 120 years after death
        int dateOfDeathDad = genDeathYear(dateOfBirthDad);

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(genEvent(mom, "birth", dateOfBirthMom));
            eventDao.insert(genEvent(dad, "birth", dateOfBirthDad));
            eventDao.insert(genEvent(mom, "marriage", dateOfMarriage, locationOfMarriage));
            eventDao.insert(genEvent(dad, "marriage", dateOfMarriage, locationOfMarriage));
            eventDao.insert(genEvent(mom, "death", dateOfDeathMom));
            eventDao.insert(genEvent(dad, "death", dateOfDeathDad));
            db.closeConnection(true);
            eventsAdded += 6;
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        numGenerations -= 1;
        if (numGenerations > 0) {
            genGenerations(mom, numGenerations);
            genGenerations(dad, numGenerations);
        }

        try (Connection conn = db.getConnection()) {
            PersonDao personDao = new PersonDao(conn);
            if (personDao.get(child.getPersonID()) == null) {
                personDao.insert(child);
                personsAdded += 1;
            }
            if (numGenerations == 0) {
                personDao.insert(mom);
                personDao.insert(dad);
                personsAdded += 2;
            }

            db.closeConnection(true);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a birth year (in relation to the child's birth year)
     * @param child The child of the person
     * @return Birth year of the parent
     */
    private int genBirthYear(Person child) throws DataAccessException {
        Database db = new Database();
        try (Connection conn = db.getConnection()) {

            EventDao eventDao = new EventDao(conn);
            int childBirthYear = eventDao.get(child.getPersonID(), "birth").getYear();

            db.closeConnection(true);

            int low = childBirthYear - 50;
            int high = childBirthYear - 20;
            int newBirthYear = r.nextInt(high - low) + low;
            System.out.println("New birthyear for parent is : " + newBirthYear + " (Child was born: " + childBirthYear + ")");
            return newBirthYear;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error in generation - generating a birth year");
        }
    }

    /**
     * Generates the death year for the parent (in relation to the person's birth year)
     * @param birthYear Birth year of the person
     * @return Death year of the person
     */
    private int genDeathYear(int birthYear) {
        int low = birthYear + 70;
        int high = birthYear + 96;
        int deathYear = r.nextInt(high - low) + low;
        System.out.println("Died on year: " + deathYear + " (Died at age " + (deathYear - birthYear) + ")");
        return deathYear;
    }

    /**
     * Generate a marriage year (based off of the two persons' birth years)
     * @param birthYear1 Birth year of the first person
     * @param birthYear2 Birth year of the second person
     * @return The marriage date for the couple
     */
    private int genMarriageYear(int birthYear1, int birthYear2) {
        //Pick the youngest of the two birth years (to ensure no funky marriage dates)
        int youngestBirthYear = Math.max(birthYear1, birthYear2);

        Random r = new Random();
        int low = youngestBirthYear + 20;
        int high = youngestBirthYear + 35;
        int marriageYear = r.nextInt(high - low) + low;
        System.out.println("Married on year: " + marriageYear + " at age: " + (marriageYear - birthYear1) +
                " and " + (marriageYear - birthYear2) + ")");
        return marriageYear;
    }

    /**
     * Generate a person
     * @param associatedUsername Who this person object belongs to (the current user's username)
     * @param gender The gender of the person we want to generate
     * @return The generated person
     */
    private Person genPerson(String associatedUsername, String gender) {
        String personID = UUID.randomUUID().toString();
        Random r = new Random();
        String firstName = "";
        if (gender.equals("f")) {
            firstName = femaleNames.get(r.nextInt(femaleNames.size()));
        } else if (gender.equals("m")) {
            firstName = maleNames.get(r.nextInt(maleNames.size()));
        } else {
            System.out.println("ERROR!!!!  The gender wasn't detected to be f or m - Generation.java");
        }

        String lastName = surNames.get(r.nextInt(surNames.size()));

        return new Person(personID, associatedUsername, firstName, lastName, gender);
    }

    /**
     * Generate an event wrapper that auto-creates a random location
     * @param person The person who's event it is
     * @param type The type of event (birth, death, marriage, etc)
     * @param year Year of the event
     * @return The generated event
     */
    public Event genEvent(Person person, String type, int year) {
        //Do the real genEvent but with a random location
        return genEvent(person, type, year, locations.get(r.nextInt(locations.size())));
    }

    /**
     * Generate a random event
     * @param person Person who's event it is
     * @param type Type of event (birth, death, marriage, etc)
     * @param year Year of the event
     * @param location Location of the event
     *@return The generated event
     */
    private Event genEvent(Person person, String type, int year, Location location) {
        String eventID = UUID.randomUUID().toString();
        String associatedUsername = person.getAssociatedUsername();
        String personID = person.getPersonID();
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        String country = location.getCountry();
        String city = location.getCity();

        return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, type, year);
    }

    /**
     * Get the count of persons added
     * @return Count of the persons added
     */
    public int getPersonsAdded() {
        return personsAdded;
    }

    /**
     * Get the count of events added
     * @return Count of the events added
     */
    public int getEventsAdded() {
        return eventsAdded;
    }
}
