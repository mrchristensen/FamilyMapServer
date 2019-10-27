package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;


public class FileRequestHandler implements HttpHandler{

    // Handles HTTP requests containing the "/games/list" URL path.
    // The "exchange" parameter is an HttpExchange object, which is
    // defined by Java.
    // In this context, an "exchange" is an HTTP request/response pair
    // (i.e., the client and server exchange a request and response).
    // The HttpExchange object gives the handler access to all of the
    // details of the HTTP request (Request type [GET or POST],
    // request headers, request body, etc.).
    // The HttpExchange object also gives the handler the ability
    // to construct an HTTP response and send it back to the client
    // (Status code, headers, response body, etc.).
    @Override
    public void handle(HttpExchange exchange) throws IOException {


        // This handler returns a list of "Ticket to Ride" games that
        // are (ficticiously) currently running in the server.
        // The game list is returned to the client in JSON format inside
        // the HTTP response body.
        // This implementation is clearly unrealistic, because it
        // always returns the same hard-coded list of games.
        // It is also unrealistic in that it accepts only one specific
        // hard-coded auth token.
        // However, it does demonstrate the following:
        // 1. How to get the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to return the desired status code (200, 404, etc.)
        //		in an HTTP response
        // 4. How to write JSON data to the HTTP response body
        // 5. How to check an incoming HTTP request for an auth token

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow GET requests for this operation.
            // This operation requires a GET request, because the
            // client is "getting" information from the server, and
            // the operation is "read only" (i.e., does not modify the
            // state of the server).
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                File myFile;
                URI path = exchange.getRequestURI();
                System.out.println("\nGET Request\nURI (path): '" + path + "'");
                System.out.println("URI path.toString: '" + path.toString() + "'");

                if(path.toString().equals("/")){
                    System.out.println("URI: '/'");
                    myFile = new File("web/index.html");
                }
                else{
                    myFile = new File("web" + path);
                }

                if(myFile.exists()) {
                    System.out.println("File exists\nServing: " + myFile.getPath());
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    //TODO: server()
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(myFile.toPath(), respBody);
                    writeString(myFile.toString(), respBody);
                    exchange.close();

                } else {
                    System.out.println("Error in serving file (doesn't exist - 404): " + myFile.getPath());
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                    //TODO: serve("web/HTML/404.html");
                    myFile = new File("web/HTML/404.html");
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(myFile.toPath(), respBody);
                    writeString(myFile.toString(), respBody);
                    exchange.close();

                }





            } else {
                // We expected a GET but got something else, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
    The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}
