package handlers;

import com.google.gson.Gson;

public class JsonDeserialization {
    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }

    //TODO: clean up or implement
    public static String serialize(Object myObject) {
        return new Gson().toJson(myObject);
    }
}
