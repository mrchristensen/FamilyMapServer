package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.AuthTokenDao;
import dataaccess.Database;
import dataaccess.EventDao;
import exceptions.DataAccessException;
import result.*;
import service.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class EventRequestHandler implements HttpHandler {
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
        System.out.println("\n\t- Event Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is get");
        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
            System.out.println("Request method is get");

            Result result = null;
//TODO: Check the authtoken
            //See if we're looking for a single event or all that belong to a user
            String[] args = exchange.getRequestURI().toString().split("(?!^)/");
            if (args.length > 1) {
                System.out.println("Get a single event (with id of): " + args[1]);
                result = new EventIDService().retrieveEvent(args[1]);
            } else {
                System.out.println("Get all events of the current user (from auth token)");
                String authKey = exchange.getRequestHeaders().getFirst("Authorization"); //Todo

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
                    result = new EventService().retrieveAllEvents(associatedUsername);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Write the response from the server
            System.out.println("Finished finding all events for the current user");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();
            String json = JsonDeserialization.serialize(result);
            respBody.write(json.getBytes());

            exchange.close();


//            if (clearResult.getMessage().equals("Clear succeeded.")) {
//                System.out.println("Clear was successful.\nClearResult message: " + clearResult.getMessage());
//                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
//
//                //Write the response from the server
//                OutputStream respBody = exchange.getResponseBody();
//                String json = JsonDeserialization.serialize(clearResult);
//                respBody.write(json.getBytes());
//
//                exchange.close();
//            } else { //Error
//                System.out.println("Error during clear: " + clearResult.getMessage());
//                exchange.sendResponseHeaders(HttpURLConnection.HTTP_PRECON_FAILED, 0);
//
//                //Write the response from the server
//                OutputStream respBody = exchange.getResponseBody();
//                String json = JsonDeserialization.serialize(clearResult);
//                respBody.write(json.getBytes());
//
//                exchange.close();
//            }
        }
    }
}
