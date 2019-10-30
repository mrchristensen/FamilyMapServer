package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.AuthTokenDao;
import dataaccess.Database;
import result.FillResult;
import result.Result;
import service.FillService;
import service.PersonIDService;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class FillRequestHandler implements HttpHandler {
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
        System.out.println("\n\t- Fill Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is post");
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            System.out.println("Request method is post");
            FillResult result = new FillResult();
            String[] args = exchange.getRequestURI().toString().split("(?!^)/");

            String userName = args[1];
            int numGenerations = 0;

            //Check to see if numGenerations arg was given
            if (args.length == 3) {
                numGenerations = Integer.parseInt(args[2]);
            }
            else {
                numGenerations = 4; //Default is 4 generations
            }

            try {
                result = new FillService().fillGenerations(userName, numGenerations);
            } catch (SQLException e) {
                e.printStackTrace();
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
