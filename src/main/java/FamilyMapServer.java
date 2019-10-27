import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {

    public static void main(String[] args){
        try{
            startServer(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }
    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
//        server.createContext("/user/register", new Register.RequestHandler());



    }
}
