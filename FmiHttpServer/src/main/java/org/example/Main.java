package org.example;

import org.example.controllers.CustomerController;
import org.example.controllers.HomeController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {


    private static final String NEW_LINE = "\n\r";

    private static String buildHttpResponse(String body) {
        return "HTTP/1.1 200 OK" + NEW_LINE +
                "Access-Control-Allow-Origin: *" + NEW_LINE +
                "Content-Length: " + body.getBytes().length + NEW_LINE +
                "Content-Type: text/html" + NEW_LINE + NEW_LINE +
                body + NEW_LINE + NEW_LINE;
    }

    public static void main(String[] args) throws IOException {
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
//                System.out.println(currentLine);
            }

            if(httpMethod.equals("GET") && httpEndpoint.equals("/home")){
                HomeController homeController = new HomeController();
                controllerMessage = homeController.index();
            }

            if(httpMethod.equals("GET") && httpEndpoint.equals("/customer")){
                CustomerController customerController = new CustomerController();
                controllerMessage = customerController.index();
            }

            response.write(buildHttpResponse(controllerMessage).getBytes());
            request.close();
            response.close();
            connectionSocket.close();
        }
    }
}