package handlers;

import com.sun.net.httpserver.HttpExchange;
import request.LoadRequest;
import result.LoadResult;
import result.Result;
import service.LoadService;

import java.io.IOException;

import static handlers.JsonDeserialization.inputStreamToString;

public class LoadRequestHandler extends RequestHandler {
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
        System.out.println("\n\t- Load Request Handler -");

        // Determine the HTTP request type (GET, POST, etc.)
        if (!verifyRequestMethod(exchange, "POST")) {
            Result result = new Result();
            result.setMessage("Error: Invalid Request Method");
            writeOutput(exchange, result);
            exchange.close();
            return;
        }

        String jsonString = inputStreamToString(exchange.getRequestBody());

        LoadRequest request = JsonDeserialization.deserialize(jsonString, LoadRequest.class);
        LoadResult loadResult = (LoadResult) new LoadService().load(request);

        //Send the response
        writeOutput(exchange, loadResult);
        exchange.close();
    }

}