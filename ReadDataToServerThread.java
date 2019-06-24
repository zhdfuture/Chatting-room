package com.zh.client.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadDataToServerThread extends Thread {
    private final Socket socket;

    public ReadDataToServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream in = this.socket.getInputStream();
            Scanner scan = new Scanner(in);
            while (true) {
                try {
                    String message = scan.nextLine();
                    System.out.println("来自服务端：" + message);

                } catch (NoSuchElementException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}