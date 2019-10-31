package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.Database;
import exceptions.DataAccessException;
import result.ClearResult;
import result.Result;
import service.ClearService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.sql.SQLException;

public class ClearRequestHandler implements HttpHandler {
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
        System.out.println("\n\t- Clear Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is post");
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            System.out.println("Request method is post");

            Result clearResult;
            clearResult = new ClearService().clearDatabase();

            if (clearResult.getMessage().equals("Clear succeeded.")) {
                System.out.println("Clear was successful.\nClearResult message: " + clearResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(clearResult);
                respBody.write(json.getBytes());

                exchange.close();
            } else { //Error
                System.out.println("Error during clear: " + clearResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(clearResult);
                respBody.write(json.getBytes());

                exchange.close();
            }
        }
    }
}
