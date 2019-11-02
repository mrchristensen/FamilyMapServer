package handlers;

import com.sun.net.httpserver.HttpExchange;
import result.Result;
import service.EventIDService;
import service.EventService;

import java.io.IOException;

public class EventRequestHandler extends RequestHandler {
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
        if (!verifyRequestMethod(exchange, "GET")) {
            Result result = new Result();
            result.setMessage("Error: Invalid Request Method");
            writeOutput(exchange, result);
            exchange.close();
            return;
        }

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is get");
        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
            Result eventResult = new Result();
            System.out.println("Request method is get");

            //Validate auth token
            String authKey = exchange.getRequestHeaders().getFirst("Authorization");
            String associatedUsername = verifyAuthToken(authKey);
            if (associatedUsername == null) {
                System.out.println("Invalid auth token in Person Request");
                eventResult.setMessage("Error: Invalid auth token");
                writeOutput(exchange, eventResult);
                exchange.close();
                return;
            }

            //See if we're looking for a single event or all that belong to a user
            String[] args = exchange.getRequestURI().toString().split("(?!^)/");
            if (args.length > 1) {
                System.out.println("Get a single event (with id of): " + args[1]);
                eventResult = new EventIDService().retrieveEvent(args[1], associatedUsername);
            } else {
                System.out.println("Get all events of the current user (from auth token)");
                eventResult = new EventService().retrieveAllEvents(associatedUsername);
            }

            //Write the response from the server
            System.out.println("Finished finding event(s)");
            writeOutput(exchange, eventResult);
            exchange.close();
        }
    }

}
