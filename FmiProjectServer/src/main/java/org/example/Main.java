package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int EXIT_CODE = 1;
    private static final int COUNT_BREAK = 10;
    public static final int COMMON_CODE = 9;

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(1423);
        System.out.println("Server Start");

        Socket connectionSocket = serverSocket.accept();

        System.out.println("Waiting for connection");

        InputStream request = connectionSocket.getInputStream();
        OutputStream response = connectionSocket.getOutputStream();

        int itrCount = 0;

        while(connectionSocket.isConnected()){
            itrCount++;
            int responseToken = request.read();

            System.out.println("Response: " + responseToken);

            if(itrCount == COUNT_BREAK){
                response.write(EXIT_CODE);
            }else{
                response.write(COMMON_CODE);
            }
        }

        request.close();
        response.close();
        serverSocket.close();
    }
}