package minispring.boot.web.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    private final HttpServer server;

    public WebServer(int port, HttpHandler handler) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", handler);
    }

    public void start() {
        server.start();
        System.out.println("Started on port " + server.getAddress().getPort());
    }
}
