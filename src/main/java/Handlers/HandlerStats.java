package Handlers;

import Server.Link;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class HandlerStats implements HttpHandler {

    private static final String OBJECT_NOT_FOUND = "Object not found!";
    private static final int MAX_RANK = 1;
    private static final int COUNT_PLUS = 1;

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
        int initialRate = 0;
        int positionRate = 0;

        for (Map.Entry<String, Link> entry : HandlerGenerate.mapOfLinks.entrySet()) {
            if (keyAndValue[1].equals(entry.getValue().getLink())) {
                // Plus count
                entry.getValue().setCount(COUNT_PLUS);
                // Rating
                for (Map.Entry<String, Link> entry1 : HandlerGenerate.mapOfLinks.entrySet()) {
                    if (entry1.getValue().getCount() >= initialRate) {
                        entry1.getValue().setRank(MAX_RANK);
                        initialRate = entry1.getValue().getCount();
                        positionRate = entry1.getValue().getRank();
                    } else if (entry1.getValue().getCount() < initialRate) {
                        entry1.getValue().setRank(positionRate + 1);
                        positionRate = entry1.getValue().getRank();
                    }
                }
                positionRate = 0;
                initialRate = 0;
                // Return object
                return entry.getValue()
                        .toString();
            }
        }
        return null;
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        // Monitoring
        monitoring(httpExchange);
        return null;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        if (requestParamValue != null) {
            sendResponse(httpExchange, 200, requestParamValue);
        } else requestParamValue = OBJECT_NOT_FOUND;
        sendResponse(httpExchange, 404, requestParamValue);
    }

    // Other methods

    private String[] getKeyAndValueOfRequest(HttpExchange httpExchange) {
        String originalLink = httpExchange.getRequestURI().toString();
        String request = originalLink.substring(originalLink.indexOf("?"));
        return request.split("=");
    }

    private static void sendResponse(HttpExchange httpExchange, int rCode, String requestParamValue) throws IOException {
        httpExchange.sendResponseHeaders(rCode, requestParamValue.length());
        httpExchange.getResponseBody().write(requestParamValue.getBytes());
        httpExchange.getResponseBody().flush();
        httpExchange.getResponseBody().close();
    }

    private static void monitoring(HttpExchange httpExchange) {
        System.out.println(httpExchange.getRequestHeaders().entrySet());
        System.out.println(httpExchange.getRequestMethod());
        System.out.println(httpExchange.getRequestURI());
        System.out.println(httpExchange.getRequestBody());
    }

    public static void countRate(int firstRate) {
        for (Map.Entry<String, Link> entry : HandlerGenerate.mapOfLinks.entrySet()) {
            if (firstRate < entry.getValue().getCount()) {
                entry.getValue().setRank(firstRate + 1);
            } else entry.getValue().setRank(MAX_RANK);
        }
    }
}