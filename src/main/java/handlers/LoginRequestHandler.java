package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import request.*;
import result.*;
import service.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.SQLException;


public class LoginRequestHandler implements HttpHandler {
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
        System.out.println("\n\t- Login Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is post");
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            System.out.println("Request method is post");

            String jsonString = exchange.getRequestBody().toString();
            LoginRequest request = JsonDeserialization.deserialize(jsonString, LoginRequest.class);
            LoginResult loginResult = null;
            try {
                loginResult = new LoginService().loginUser(request);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (loginResult.getMessage() == null) { //If the error message is null
                System.out.println("Login was a success." +
                        "\nauthToken: " + loginResult.getAuthToken() +
                        "\nuserName: " + loginResult.getUserName() +
                        "\npersonID: " + loginResult.getPersonID());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.close();
            } else { //Error
                System.out.println("Error during login: " + loginResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_PRECON_FAILED, 0);
                exchange.close();
            }
        }

    }

}
