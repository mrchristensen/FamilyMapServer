package generation;

import com.google.gson.*;
import dataaccess.Database;
import dataaccess.EventDao;
import dataaccess.PersonDao;
import exceptions.DataAccessException;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.parser.JSONParser;
import model.Event;
import model.Person;
import org.json.JSONArray;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Generation {

    List<String> femaleNames;
    List<String> maleNames;
    List<String> surNames;

    public Generation() {
        femaleNames = jsonToString(new File("json/fnames.json"));
        maleNames = jsonToString(new File("json/mnames.json"));
        surNames  = jsonToString(new File("json/snames.json"));
    }

    public void genGenerations(Person child, int numGenerations) throws IOException, SQLException, DataAccessException {

        Person dad = genPerson(child.getAssociatedUsername(), "m");
        Person mom = genPerson(child.getAssociatedUsername(), "f");
        mom.setSpouseID(dad.getPersonID());
        dad.setSpouseID(mom.getPersonID());
        child.setMotherID(mom.getMotherID());
        child.setFatherID(dad.getFatherID());

        if(numGenerations > 0){
            genGenerations(mom, numGenerations - 1);
            genGenerations(dad, numGenerations - 1);
        }

        int dateOfBirthMom = genBirthYear(child); //More than 13 before child's date of birth
        int dateOfBirthDad = genBirthYear(child);
        int dateOfMarriage = genMarriageYear(dateOfBirthMom, dateOfBirthDad); //At least 19 years after either's birthday
        int dateOfDeathMom = genDeathYear(dateOfBirthMom); //After the birth of child, not more than 120 years after death
        int dateOfDeathDad = genDeathYear(dateOfBirthDad);


        Database db = new Database();
        Connection conn = db.openConnection();

        PersonDao personDao = new PersonDao(conn);
        if(personDao.get(child.getPersonID()) == null){
            personDao.insert(child);
        }
        if(numGenerations == 0) {
            personDao.insert(mom);
            personDao.insert(dad);
        }

        EventDao eventDao = new EventDao(conn);
        eventDao.insert(genEvent(mom, "Birth", dateOfBirthMom));
        eventDao.insert(genEvent(dad, "Birth", dateOfBirthDad));
        eventDao.insert(genEvent(mom, "Marriage", dateOfMarriage));
        eventDao.insert(genEvent(dad, "Marriage", dateOfMarriage));
        eventDao.insert(genEvent(mom, "Death", dateOfDeathMom));
        eventDao.insert(genEvent(dad, "Death", dateOfDeathDad));

        db.closeConnection(true);

    }

    int genBirthYear(Person child) throws SQLException, DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        EventDao eventDao = new EventDao(conn);
        int childBirthyear = eventDao.get(child.getAssociatedUsername(), "Birth").getYear();
        db.closeConnection(true);

        Random r = new Random();
        int low = childBirthyear - 45;
        int high = childBirthyear - 20;
        int newBirthyear =  r.nextInt(high-low) + low;
        System.out.println("New birthyear for parent is : " + newBirthyear + " (Child was born: " + childBirthyear + ")");
        return newBirthyear;
    }

    int genDeathYear(int birthYear) throws SQLException, DataAccessException {
        Random r = new Random();
        int low = birthYear + 75;
        int high = birthYear + 95;
        int deathYear =  r.nextInt(high-low) + low;
        System.out.println("Died on year: " + deathYear + " (Died at age " + (deathYear-birthYear) + ")");
        return deathYear;
    }

    int genMarriageYear(int birthYear1, int birthYear2){

        int youngestBirthYear;

        //Pick the youngest of the two birth years (to ensure no funky marriage dates)
        if(birthYear1 > birthYear2){
            youngestBirthYear = birthYear1;
        }
        else{
            youngestBirthYear = birthYear2;
        }

        Random r = new Random();
        int low = youngestBirthYear + 20;
        int high = youngestBirthYear + 35;
        int marriageYear =  r.nextInt(high-low) + low;
        System.out.println("Married on year: " + marriageYear + " at age: " + (marriageYear - birthYear1) +
                            " and " + (marriageYear - birthYear2) + ")");
        return marriageYear;
    }

    /**
     * Generate a person
     */
    Person genPerson(String associatedUsername, String gender) throws IOException {
        String personID = UUID.randomUUID().toString();
        Random rand = new Random();
        String firstName = "";
        if(gender.equals("f")){
            firstName = femaleNames.get(rand.nextInt(femaleNames.size()));
        }
        else if(gender.equals("m")) {
            firstName = maleNames.get(rand.nextInt(maleNames.size()));
        }
        else{
            System.out.println("ERROR!!!!  The gender wasn't detected to be f or m - Generation.java");
        }

        String lastName = surNames.get(rand.nextInt(surNames.size()));

        return new Person(personID, associatedUsername, firstName, lastName, gender);
    }

    Event genEvent(Person person, String type, int year){
        String eventID = UUID.randomUUID().toString();
        String associatedUsername = person.getAssociatedUsername();
        String personID = person.getPersonID();
        float latitude = 0; //Todo
        float longitude = 0; //Todo
        String country = "0"; //Todo
        String city = "0"; //Todo

        return new Event(eventID, associatedUsername, personID, latitude, longitude , country, city, type, year);
    }

    public List<String> jsonToString(File myFile){
        String json = "";
        try{
            json = new String(Files.readAllBytes(myFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
        JsonArray jsonArray = (JsonArray) jsonObject.get("data");
        List<String> myList = new ArrayList<>();
        System.out.println("\n\tAdding elements from " + myFile.getName());
        for (JsonElement element : jsonArray) {
            System.out.println("Added: " + element);
            myList.add(element.toString());
        }

        return myList;

    }
}
