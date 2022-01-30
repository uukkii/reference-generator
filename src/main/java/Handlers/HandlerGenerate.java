package Handlers;

import Server.Link;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HandlerGenerate implements HttpHandler {

    protected static final Map<String, Link> mapOfLinks = new HashMap<>();
    private static final int LENGTH_OF_LINK = 10;
    private static final int DEFAULT_RANK = 0;
    private static final int DEFAULT_COUNT = 0;
    private static final String SET_OF_CHARACTERS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    private static final String OBJECT_NOT_FOUND = "Object not found!";
    private static StringBuilder shortLink;

    public static void sendResponse(HttpExchange httpExchange, int rCode, String requestParamValue) throws IOException {
        httpExchange.sendResponseHeaders(rCode, requestParamValue.length());
        httpExchange.getResponseBody().write(requestParamValue.getBytes());
        httpExchange.getResponseBody().flush();
        httpExchange.getResponseBody().close();
    }

    public static void monitoring(HttpExchange httpExchange) {
        System.out.println(httpExchange.getRequestHeaders());
        System.out.println(httpExchange.getRequestMethod());
        System.out.println(httpExchange.getRequestURI());
        System.out.println(httpExchange.getRequestBody());
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;

        if (httpExchange.getRequestMethod().equals("GET")) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if (httpExchange.getRequestMethod().equals("POST")) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        // Monitoring
        monitoring(httpExchange);

        // Object check
        String[] keyAndValue = getKeyAndValueOfRequest(httpExchange);
        for (Map.Entry<String, Link> entry : mapOfLinks.entrySet()) {
            if (keyAndValue[1].equals(entry.getValue().getOriginal()))
                return entry.getValue().getLink();
        }
        return null;
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        // Monitoring
        monitoring(httpExchange);

        // Creating object
        String[] keyAndValue = getKeyAndValueOfRequest(httpExchange);
        Link newLink = new Link(
                keyAndValue[1],
                generateLinkName(),
                DEFAULT_RANK,
                DEFAULT_COUNT
        );
        mapOfLinks.put(keyAndValue[0], newLink);

        // Return requestParamValue
        return newLink.getLink();
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        if (requestParamValue != null) {
            String response = "{\n" +
                    "\"link\" " + "\"" + requestParamValue + "\"" +
                    "\n}";
            sendResponse(httpExchange, 200, response);
        } else requestParamValue = OBJECT_NOT_FOUND;
        sendResponse(httpExchange, 404, requestParamValue);
    }

    // Other methods
    private String generateLinkName() {
        shortLink = new StringBuilder();
        for (int i = 0; i < LENGTH_OF_LINK; i++) {
            int randInt = new Random().nextInt(SET_OF_CHARACTERS.length());
            char randChar = SET_OF_CHARACTERS.charAt(randInt);
            shortLink.append(randChar);
        }
        return "/1/" + shortLink;
    }

    private String[] getKeyAndValueOfRequest(HttpExchange httpExchange) {
        String originalLink = httpExchange.getRequestURI().toString();
        String request = originalLink.substring(originalLink.indexOf("?"));
        return request.split("=");
    }
}