package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.AuthTokenDao;
import dataaccess.Database;
import exceptions.DataAccessException;
import result.Result;
import service.PersonIDService;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

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
        try {
            System.out.println("\n\t- Person Request Handler -");

            // Determine the HTTP request type (GET, POST, etc.)
            System.out.println("Check so see if the request method is get");
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                System.out.println("Request method is get");
                Result result = new Result();
                String[] args = exchange.getRequestURI().toString().split("(?!^)/");
                String authKey = exchange.getRequestHeaders().getFirst("Authorization");

                //Database and setup
                Database db = new Database();
                Connection conn;
                conn = db.openConnection();
                AuthTokenDao authTokenDao = new AuthTokenDao(conn);
                String associatedUsername;

                associatedUsername = authTokenDao.getAuthUsername(authKey);


                db.closeConnection(true);


                //Validate auth token
                if (associatedUsername == null) {
                    System.out.println("Invalid auth token in Person Request");
                    result.setMessage("Error: Invalid auth token");
                    writeOutput(exchange, result);
                    return;
                }

                //See if we're looking for a single person or all that belong to a user
                if (args.length > 1) {
                    System.out.println("Get a single person (with id of): " + args[1]);
                    result = new PersonIDService().retrievePerson(args[1], associatedUsername);
                } else {
                    System.out.println("Get all relatives of the current user (from auth token)");
                    result = new PersonService().retrieveAllPersons(associatedUsername);
                }

                //Write the response from the server
                System.out.println("Finished finding person(s)");
                writeOutput(exchange, result);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void writeOutput(HttpExchange exchange, Result result) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream respBody = exchange.getResponseBody();
        String json = JsonDeserialization.serialize(result);
        respBody.write(json.getBytes());

        exchange.close();
    }
}
