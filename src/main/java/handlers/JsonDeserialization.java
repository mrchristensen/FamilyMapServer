package handlers;

import com.google.gson.*;
import model.Location;
import model.Locations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonDeserialization {
    /**
     * Deserialize a json string to an object
     * @param value Json String
     * @param returnType Class type
     * @param <T> Class type
     * @return Class object
     */
    static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }

    /**
     * Serialize an object to a json string
     * @param myObject Object to serialize
     * @return Json string
     */
    static String serialize(Object myObject) {
        return new Gson().toJson(myObject);
    }

    /**
     * Convert a json file to a string
     * @param myFile Json file
     * @return String with the contents of the json file
     */
    public static List<String> jsonToStrings(File myFile) {
        String json = "";
        try {
            json = new String(Files.readAllBytes(myFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
        JsonArray jsonArray = (JsonArray) jsonObject.get("data");
        List<String> myList = new ArrayList<>();
        System.out.println("Adding elements from " + myFile.getName());

        for (JsonElement element : jsonArray) {
            myList.add(element.toString().replaceAll("\"", "")); //Replace all get rid of extra quotes
        }

        return myList;
    }

    /**
     * Take a json file and turn it into a list of locations
     * @param myFile Json file
     * @return List of locations
     */
    public static List<Location> jsonToLocations(File myFile) {
        String json = "";
        try {
            json = new String(Files.readAllBytes(myFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Locations data = JsonDeserialization.deserialize(json, Locations.class);
        List<Location> myList = new ArrayList<>();

        Location[] locations = data.getData();
        Collections.addAll(myList, locations);
        return myList;
    }

    static String inputStreamToString(InputStream inputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString(StandardCharsets.UTF_8);
    }

}
