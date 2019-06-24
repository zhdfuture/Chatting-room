package com.zh.client.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WriteDataToServerThread extends Thread {
    private final Socket socket;
    public WriteDataToServerThread(Socket socket) {
        this.socket=socket;
    }
    public void run(){
        try {
            OutputStream out=this.socket.getOutputStream();
            PrintStream  printStream=new PrintStream(out);
            Scanner scan=new Scanner(System.in);
            while(true){
                System.out.println("please input:");
                String message=scan.nextLine();
                printStream.println(message);
                printStream.flush(); //清空缓冲
                if(message.equals("bye")){
                    break;
                }
            }
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
