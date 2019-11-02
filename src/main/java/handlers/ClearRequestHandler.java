package handlers;

import com.sun.net.httpserver.HttpExchange;
import result.Result;
import service.ClearService;

import java.io.IOException;

public class ClearRequestHandler extends RequestHandler {
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
        if (!verifyRequestMethod(exchange, "POST")) {
            Result result = new Result();
            result.setMessage("Error: Invalid Request Method");
            writeOutput(exchange, result);
            exchange.close();
            return;
        }

        Result clearResult;
        clearResult = new ClearService().clearDatabase();

        System.out.println("Finished Clear: " + clearResult.getMessage());
        writeOutput(exchange, clearResult);
        exchange.close();
    }

}
