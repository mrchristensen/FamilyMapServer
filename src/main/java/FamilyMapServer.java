import com.sun.net.httpserver.HttpServer;
import dataaccess.Database;
import exceptions.DataAccessException;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {

    /**
     * Main function of the project
     */
    public static void main(String[] args){
        try{
            int port = Integer.parseInt(args[0]);
            Database db = new Database();
            db.getConnection();
            db.createTables(); //Ensure that the tables are created
            db.closeConnection(true);
            startServer(port);
        } catch (DataAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the server
     */
    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    /**
     * Creates register handles (to link a handler to it's request
     */
    private static void registerHandlers(HttpServer server) {
        //WebPage Command
        server.createContext("/", new FileRequestHandler());

        //Database Commands
        server.createContext("/clear", new ClearRequestHandler()); //This API will clear ALL data from the database, including users and all generated data. This API can be run from a browser by simply typing it in the address bar or by clicking this link followed by pressing the Submit button below. No authorization authToken is required.
        server.createContext("/load", new LoadRequestHandler()); //This API will load the server's database with data provided by json text in the response body. The json text must contain an array of users as defined in the register details in addition to a personID, an array of persons, and an array of events. WARNING: all data from the database is wiped when this is called. The json file will be specified in the body of the request. A example.json file is provided to get you started with loading specific data. No authorization authToken is required.
        server.createContext("/fill", new FillRequestHandler()); //This API will fill the server's database with fake data for the specified user name. The "username" parameter is required and must be a user already registered with the server. It can be any user name you choose. If there is any data in the database associated with the given user name, it is erased. This API can be run from a browser by simply typing it in the address bar (or by pressing the link, filling in the details, and clicking submit). The base person generated should be a person representing the user. No authorization authToken is required.
        //This API will fill the server's database with fake data for the specified user name. The "username" parameter is required and must be a user already registered with the server. All the ancestor data associated with this user is erased. This API can be run from a browser by simply typing it in the address bar (or by pressing the link, filling in the details, and clicking submit). The base person generated should be a person representing the user. No authorization authToken is required.

        //User Commands
        server.createContext("/user/login", new LoginRequestHandler());  //Use this to log in a user. A request body must be supplied specifying the username and password. If login succeeds, an authorization authToken will be returned. Use this authToken on other API calls that require authorization. The returned JSON object contains "Authorization" (the authorization authToken) and "username" (the username that was just logged in). No authorization authToken is required.
        server.createContext("/user/register", new RegisterRequestHandler());  //Use this to register a user. An authorization authToken is returned. Use it just as you would a authToken from login. Returns the same Json object as log in. It should be noted that when you register a user the database will automatically be filled.(Meaning you do not need to call the /fill API noted above). No authorization authToken is required.
        server.createContext("/event", new EventRequestHandler()); //This API will return ALL events for ALL family members of the current user. The current user is determined from the provided authorization authToken (which is required for this call). The returned JSON object contains "data" which is an array of event objects. Authorization authToken is required.
        //This API will return the single event with the specified ID. The event must belong to a relative of the user associated with the authorization authToken. The returned JSON contains the requested event object. Authorization authToken is required.
        server.createContext("/person", new PersonRequestHandler()); //This API will return ALL people associated with the current user. The current user is determined from the provided a uthorization authToken (which is required for this call). The returned JSON object contains "data" which is an array of person objects. Authorization authToken is required.
        //This API will return the single person with the specified ID. The person must be related to the user associated with the authorization authToken. The returned JSON contains the requested person object. Authorization authToken is required
    }
}
