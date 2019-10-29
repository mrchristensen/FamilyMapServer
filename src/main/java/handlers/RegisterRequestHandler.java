package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import request.RegisterRequest;
import result.*;
import service.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;


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
        System.out.println("\n- Register Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        System.out.println("Check so see if the request method is post");
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            System.out.println("Request method is post");

            String jsonString = exchange.getRequestBody().toString();
            RegisterRequest request = JsonDeserialization.deserialize(jsonString, RegisterRequest.class);
            RegisterResult registerResult = null;
            try {
                registerResult = new RegisterService().registerUser(request);
            } catch (DataAccessException | SQLException e) {
                e.printStackTrace();
            }

            if (registerResult.getMessage().equals("Clear succeeded.")) {
                System.out.println("Clear was successful.\nClearResult message: " + registerResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.close();
            } else { //Error
                System.out.println("Error during clear: " + registerResult.getMessage());
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_PRECON_FAILED, 0);
                exchange.close();
            }
        }
    }
    }

    /**
    *The writeString method writes a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}
