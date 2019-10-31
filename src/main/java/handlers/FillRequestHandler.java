package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.FillResult;
import result.Result;
import service.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

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
            FillResult result;
            String[] args = exchange.getRequestURI().toString().split("(?!^)/");

            String userName = args[1];
            int numGenerations;

            //Check to see if numGenerations arg was given
            if (args.length == 3) {
                numGenerations = Integer.parseInt(args[2]);
            }
            else {
                numGenerations = 4; //Default is 4 generations (if numGens is NOT provided)
            }

            result = (FillResult) new FillService().fillGenerations(userName, numGenerations);

            //Write the response from the server
            System.out.println("Finished finding person(s)");
            writeOutput(exchange, result);
        }
    }

    private void writeOutput(HttpExchange exchange, Result result) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream respBody = exchange.getResponseBody();
        String json = JsonDeserialization.serialize(result);
        respBody.write(json.getBytes());

        exchange.close();
    }
}
