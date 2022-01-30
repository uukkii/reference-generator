package Handlers;

import Server.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HandlerGenerate implements HttpHandler {

    private static final int LENGTH_OF_LINK = 10;
    private static final int DEFAULT_RANK = 0;
    private static final int DEFAULT_COUNT = 0;
    private static final String SET_OF_CHARACTERS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    private static final Map<String, Link> mapOfLinks = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;

        if (httpExchange.getRequestMethod().equals("GET")) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if (httpExchange.equals("POST")) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        System.out.println("1 - " + httpExchange.getRequestHeaders().entrySet());
        System.out.println("2 - " + httpExchange.getRequestMethod());
        System.out.println("3 - " + httpExchange.getRequestURI());
        System.out.println("4 - " + httpExchange.getRequestBody());
        return httpExchange.getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        // Monitoring
        System.out.println(httpExchange.getRequestHeaders());
        System.out.println(httpExchange.getRequestMethod());
        System.out.println(httpExchange.getRequestURI());
        System.out.println(httpExchange.getRequestBody());

        String originalLink = httpExchange.getRequestURI().toString();
        String request = originalLink.substring(originalLink.indexOf("?"));
        String[] keyAndValue = request.split("=");
        Link newLink = new Link(
                keyAndValue[1],
                generateLinkName(),
                DEFAULT_RANK,
                DEFAULT_COUNT
        );
        mapOfLinks.put(keyAndValue[0], newLink);

        return newLink.getOriginal();
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        while (!mapOfLinks.isEmpty()) {
            for (Map.Entry<String, Link> entry : mapOfLinks.entrySet()) {
                String response = "For link " + requestParamValue + " crated short link: "
                        + "{\n" +
                        "link" + entry.getValue().getLink() +
                        "\n}";
                httpExchange.sendResponseHeaders(200, response.length());
                httpExchange.getResponseBody().write(response.getBytes());
            }
            httpExchange.getResponseBody().flush();
            httpExchange.getResponseBody().close();
        }
    }

    public String generateLinkName() {
        StringBuilder shortLink = new StringBuilder();
        for (int i = 0; i < LENGTH_OF_LINK; i++) {
            int randInt = new Random().nextInt(SET_OF_CHARACTERS.length());
            char randChar = SET_OF_CHARACTERS.charAt(randInt);
            shortLink.append(randChar);
        }
        return "/1/" + shortLink.toString();
    }
}