package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.AuthTokenDao;
import dataaccess.Database;
import exceptions.DataAccessException;
import result.Result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

abstract class RequestHandler implements HttpHandler {
    /**
     * Write json, from a result, to the exchange (server send the message back)
     * @param exchange The current exchange
     * @param result The result that holds the information
     */
    void writeOutput(HttpExchange exchange, Result result) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream respBody = exchange.getResponseBody();
        String json = JsonDeserialization.serialize(result);
        respBody.write(json.getBytes());
    }

    /**
     *The writeString method writes a String to an OutputStream.
     * @param str The string to output
     * @param os The OutputString to write to
     */
    void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    /**
     * Verify the auth token and, if valid, return the user name of the owner (of the auth token)
     * @param authKey Auth key to verify
     * @return Associated username of the auth token
     */
    String verifyAuthToken(String authKey){
        try {
            Database db = new Database();
            Connection conn;
            conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String associatedUsername = authTokenDao.getAuthUsername(authKey);
            db.closeConnection(true);
            return associatedUsername;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Make sure the Request Method is what we're expecting
     * @param exchange The exchange of the server
     * @param method The expected method
     * @return True if the method matches the expected method
     */
    boolean verifyRequestMethod(HttpExchange exchange, String method){
        System.out.println("Check so see if the request method is: " + method);
        if (exchange.getRequestMethod().toUpperCase().equals(method)) {
            System.out.println("Request method is: " + method + " (as expected)");
            return true;
        }
        else{
            System.out.println("Request method is NOT: " + method);
            return false;
        }
    }



}
