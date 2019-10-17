package result;

/**
 * Response body for the person/personID API call
 */
public class PersonIDResult extends Result {
    /**
     * Name of the user account this person belongs to
     */
    String associatedUsername;

    /**
     * Person's unique ID that was requested
     */
    String personID;

    /**
     * Person's first name
     */
    String firstName;

    /**
     * Person's last name
     */
    String lastName;

    /**
     * Person's gender
     */
    char gender;

    /**
     * ID of the person's father (optional)
     */
    String fatherID;

    /**
     * ID of the person's mather (optional
     */
    String motherID;

    /**
     * ID of the person's spouse (optional)
     */
    String spouseID;

    /**
     * Default constructor
     */
    public PersonIDResult() {
    }

    /**
     * Proper constructor to generate a successful response message
     * @param associatedUsername Name of the user account this person belongs to
     * @param personID Person's unique ID that was requested
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param gender The gender of the person
     * @param fatherID The ID of the father of the person
     * @param motherID The ID of the mother of the person
     * @param spouseID The ID of the spouse of the person
     */
    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName, char gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
