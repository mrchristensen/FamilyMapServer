package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;


public class RegisterRequestHandler implements HttpHandler {
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
        System.out.println("\n\t- Register Request Handler -");

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
            String jsonString =  result.toString(StandardCharsets.UTF_8);

            RegisterRequest request = JsonDeserialization.deserialize(jsonString, RegisterRequest.class);
            RegisterResult registerResult;
            registerResult = new RegisterService().registerUser(request);
            System.out.println(registerResult.toString());

            if (registerResult.getMessage() == null) { //If the error message is null
                System.out.println("Register was a success." +
                        "\nauthToken: " + registerResult.getAuthToken() +
                        "\nuserName: " + registerResult.getUserName() +
                        "\npersonID: " + registerResult.getPersonID());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(registerResult);
                respBody.write(json.getBytes());

                exchange.close();
            } else { //Error
                System.out.println("Error during register: " + registerResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write the response from the server
                OutputStream respBody = exchange.getResponseBody();
                String json = JsonDeserialization.serialize(registerResult);
                respBody.write(json.getBytes());

                exchange.close();
            }
        }

    }

}
