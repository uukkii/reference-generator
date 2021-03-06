package Server;

import Handlers.HandlerGenerate;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private static HttpServer server;
    private static ThreadPoolExecutor threadPoolExecutor;


    public Server() throws IOException {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        server.createContext("/generate", new HandlerGenerate());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Server at " + server.getAddress().toString() + " has been started!");
    }

    public void stopServer() {
        server.stop(0);
        System.out.println("Server at " + server.getAddress().toString() + " has been stopped!");
    }

//    public void startServer() {
//        server.createContext("/generate", exchange -> {
//            System.out.println(exchange.getRequestHeaders().entrySet());
//            System.out.println(exchange.getRequestURI());
//            System.out.println(exchange.getRequestMethod());
//            System.out.println(exchange.getRequestBody());
//            String str = "It is redult";
//            exchange.sendResponseHeaders(200, str.length());
//            exchange.getResponseBody().write(str.getBytes());
//            exchange.getResponseBody().flush();
//            exchange.getResponseBody().close();
//
//
//        });
//        server.setExecutor(threadPoolExecutor);
//        server.start();
//        System.out.println("Server.Server at " + server.getAddress().toString() + " has been started!");
//    }
}
