package handlers;

import com.sun.net.httpserver.HttpExchange;
import result.Result;
import service.PersonIDService;
import service.PersonService;

import java.io.IOException;

public class PersonRequestHandler extends RequestHandler {
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
        if (!verifyRequestMethod(exchange, "GET")) {
            Result result = new Result();
            result.setMessage("Error: Invalid Request Method");
            writeOutput(exchange, result);
            exchange.close();
            return;
        }

        Result personResult = new Result();

        //Validate auth token
        String authKey = exchange.getRequestHeaders().getFirst("Authorization");
        String associatedUsername = verifyAuthToken(authKey);
        if (associatedUsername == null) {
            System.out.println("Invalid auth token in Person Request");
            personResult.setMessage("Error: Invalid auth token");
            writeOutput(exchange, personResult);
            exchange.close();
            return;
        }

        //See if we're looking for a single person or all that belong to a user
        String[] args = exchange.getRequestURI().toString().split("(?!^)/");
        if (args.length > 1) {
            System.out.println("Get a single person (with id of): " + args[1]);
            personResult = new PersonIDService().retrievePerson(args[1], associatedUsername);
        } else {
            System.out.println("Get all relatives of the current user (from auth token)");
            personResult = new PersonService().retrieveAllPersons(associatedUsername);
        }

        //Write the response from the server
        System.out.println("Finished finding person(s)");
        writeOutput(exchange, personResult);
        exchange.close();
    }

}
