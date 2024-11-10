package org.example;

import org.example.entities.RequestInfo;
import org.example.system.ApplicationLoader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class FrameworkApplication {
    private static ApplicationLoader applicationLoader = new ApplicationLoader();

    private static final String NEW_LINE = "\r\n";

    public static void run(Class mainClass) {

        try {
            bootstrap(mainClass);
            startWebServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildHttpResponse(String body) {
        return "HTTP/1.1 200 OK" + NEW_LINE +
                "Access-Control-Allow-Origin: *" + NEW_LINE +
                "Content-Length: " + body.getBytes().length + NEW_LINE +
                "Content-Type: text/html" + NEW_LINE + NEW_LINE +
                body + NEW_LINE + NEW_LINE;
    }

    private static void bootstrap(Class mainClass) throws IOException, ClassNotFoundException {
        applicationLoader.findAllClasses(mainClass.getPackageName());
    }

    private static RequestInfo parseHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader httpRequestReader = new BufferedReader(inputStreamReader);

        String currentLine;
        boolean isBodyProcessable = false;
        RequestInfo httpRequestInfo = new RequestInfo();

        while ((currentLine = httpRequestReader.readLine()) != null) {

            if (currentLine.isEmpty()) {
                isBodyProcessable = true;
                break;
            }

            if (!httpRequestInfo.hasMethodAndEndpoint()) {
                String[] httpHeaderTitleCollection = currentLine.split(" ");
                httpRequestInfo.setHttpMethod(httpHeaderTitleCollection[0]);
                httpRequestInfo.setHttpEndpoint(httpHeaderTitleCollection[1]);
                continue;
            }

            String[] headerParseCollection = currentLine.split(": ");
            String headerKey = headerParseCollection[0];
            String headerValue = headerParseCollection[1];

            httpRequestInfo.setHeader(headerKey, headerValue);
        }

        if(isBodyProcessable && httpRequestInfo.hasBodyContent()){

            int contentLength = httpRequestInfo.getContentLength();
            char[] bodyCharacterCollection = new char[contentLength];
            httpRequestReader.read(bodyCharacterCollection, 0, contentLength);

            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append(bodyCharacterCollection);

            httpRequestInfo.setBody(bodyBuilder.toString());
        }

        return httpRequestInfo;
    }

    private static void startWebServer() throws
            IOException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        ServerSocket serverSocket = new ServerSocket(1424);
        System.out.println("Server Start");

        while (serverSocket.isBound()) {
            Socket connectionSocket = serverSocket.accept();
            InputStream request = connectionSocket.getInputStream();
            OutputStream response = connectionSocket.getOutputStream();

            var httpRequest = parseHttpRequest(request);

            String controllerMessage = applicationLoader.executeController(httpRequest);

            System.out.println(controllerMessage);

            response.write(buildHttpResponse(controllerMessage).getBytes());
            request.close();
            response.close();
            connectionSocket.close();
        }
    }
}
