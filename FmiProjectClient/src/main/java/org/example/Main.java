package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    private static final int REQUEST_TOKEN = 8;
    private static final int EXIT_TOKEN = 1;

    public static void main(String[] args) throws IOException, InterruptedException {

        Socket clientSocket = new Socket("localhost", 1423);

        InputStream response = clientSocket.getInputStream();
        OutputStream request = clientSocket.getOutputStream();


        while(clientSocket.isConnected()){
            //server request
            request.write(REQUEST_TOKEN);

            int responseToken = response.read();
            //server response
            System.out.println("Server responded with: " + responseToken);
            if(responseToken == EXIT_TOKEN){
                break;
            }
            Thread.sleep(1000);
        }

        request.close();
        response.close();
        clientSocket.close();
    }
}