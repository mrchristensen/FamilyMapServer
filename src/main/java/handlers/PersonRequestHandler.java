package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.AuthTokenDao;
import dataaccess.Database;
import result.*;
import service.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class PersonRequestHandler implements HttpHandler {
    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is <code>null</code>
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("\n\t- Person Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is get");
        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
            System.out.println("Request method is get");
            Result result = new Result();
            String[] args = exchange.getRequestURI().toString().split("(?!^)/");
            String authKey = exchange.getRequestHeaders().getFirst("Authorization"); //Todo

            //Database and setup
            Database db = new Database();
            Connection conn = null;
            try {
                conn = db.openConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String associatedUsername = null;
            try {
                associatedUsername = authTokenDao.getAuthUsername(authKey);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                db.closeConnection(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Validate auth token
            if(associatedUsername == null){
                System.out.println("Invalid auth token in Person Request");
                result.setMessage("Invalid auth token");
                writeOutput(exchange, result);
                return;
            }

            //See if we're looking for a single person or all that belong to a user
            if (args.length > 1) {
                System.out.println("Get a single person (with id of): " + args[1]);
                result = new PersonIDService().retrievePerson(args[1], associatedUsername);
            } else {
                System.out.println("Get all relatives of the current user (from auth token)");
                try {
                    result = new PersonService().retrieveAllPersons(associatedUsername);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Write the response from the server
            System.out.println("Finished finding person(s)");
            writeOutput(exchange, result);
        }
    }

    void writeOutput(HttpExchange exchange, Result result) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream respBody = exchange.getResponseBody();
        String json = JsonDeserialization.serialize(result);
        respBody.write(json.getBytes());

        exchange.close();
    }
}
