package org.example;

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

            InputStreamReader inputStreamReader = new InputStreamReader(request);
            BufferedReader httpRequestReader = new BufferedReader(inputStreamReader);

            String currentLine;
            String httpMethod = null;
            String httpEndpoint = null;

            String controllerMessage = null;

            while ((currentLine = httpRequestReader.readLine()) != null) {
                String[] httpHeaderTitleCollection = currentLine.split(" ");
                httpMethod = httpHeaderTitleCollection[0];
                httpEndpoint = httpHeaderTitleCollection[1];
                break;
            }

            controllerMessage = applicationLoader.executeController(httpMethod, httpEndpoint);

            System.out.println(controllerMessage);

            response.write(buildHttpResponse(controllerMessage).getBytes());
            request.close();
            response.close();
            connectionSocket.close();
        }
    }
}
