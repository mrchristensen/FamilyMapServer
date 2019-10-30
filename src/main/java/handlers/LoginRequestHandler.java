package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import request.*;
import result.*;
import service.*;

import java.io.*;
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

            InputStream inputStream = exchange.getRequestBody();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            String jsonString =  result.toString("UTF-8");

            LoginRequest request = JsonDeserialization.deserialize(jsonString, LoginRequest.class);
            LoginResult loginResult = null;
            try {
                loginResult = new LoginService().loginUser(request);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (loginResult.getMessage() == null) { //If the error message is null
                System.out.println("Login was a success." +
                        "\nauthToken: " + loginResult.getAuthTokenString() +
                        "\nuserName: " + loginResult.getUserName() +
                        "\npersonID: " + loginResult.getPersonID());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(loginResult);
                respBody.write(json.getBytes());

                exchange.close();
            } else { //Error
                System.out.println("Error during login: " + loginResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_PRECON_FAILED, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(loginResult);
                respBody.write(json.getBytes());

                exchange.close();
            }
        }

    }

}
