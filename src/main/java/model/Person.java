package model;

/**
 *Person pojo: to represent people/ancestors in the database
 */
public class Person {

    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
    }

    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    /**
     * Unique identifier for this person (non-empty string)
     */
    String personID;

    /**
     * User (Username) to which this person belongs
     */
    String associatedUsername;

    /**
     * Person's first name (non-empty string)
     */
    String firstName;

    /**
     * Person's last name (non-empty string)
     */
    String lastName;

    /**
     * Person's gender (string: "f" or "m")
     */
    String gender;

    /**
     * Person ID of person's father (possibly null)
     */
    String fatherID;

    /**
     * Person ID of person's mother (possibly null)
     */
    String motherID;

    /**
     * Person ID of person's spouse (possibly null)
     */
    String spouseID;
}
