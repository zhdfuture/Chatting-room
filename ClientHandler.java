package com.zh.server.multi;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import static com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver.register;

public class ClientHandler implements Runnable {
    private static final Map<String,Socket> socketMap=new ConcurrentHashMap<>();  //使用ConcurrentHashMap，共享，保证线程安全，加syn..也可以，但ConcurrentHashMap最优
    private final Socket client;
    private String name;
    public ClientHandler(Socket client){
        this.client=client;
    }

    @Override
    public void run() {

        try {
            InputStream in=this.client.getInputStream();

            Scanner scan=new Scanner(in);
            while(true){   //以什么标志开始
                String line=scan.nextLine();
                if(line.startsWith("register:")){
                    String[] segments=line.split(":");            //以冒号分割
                    if(segments.length==2&&segments[0].equals("register")){
                        String name=segments[1];
                        register(name);
                    }
                    continue;
                }
                if(line.startsWith("groupChat:")){
                    String[] segments=line.split(":");
                    if(segments.length==2&&segments[0].equals("groupChat")) {
                        String message = segments[1];
                        groupChat(message);
                    }
                    continue;
                }
                if(line.startsWith("privateChat:")){
                    String[] segments=line.split(":");
                    if(segments.length==3&&segments[0].equals("privateChat")) {
                        String name = segments[1];
                        String message=segments[2];
                        privateChat(name,message);
                    }
                    continue;
                }
                if(line.equalsIgnoreCase("bye:")){
                    quitChat();  //退出聊天
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

     }
    //方法的实现
    private void quitChat() {
        socketMap.remove(this.name);
        System.out.println(this.name+"下线了");
        printOnlineClient();
    }

    private void printOnlineClient() {
        System.out.println("当前在线的客户端有:"+socketMap.size()+"个，名称列表如下:");
        for(String name:socketMap.keySet()){
            System.out.println(name);
        }
    }

    private void privateChat(String name, String message) {
        Socket socket=socketMap.get(name);
        if(socket!=null){
            sendMessage(socket,this.name+"说:"+message);
        }
    }

    private void sendMessage(Socket socket, String message) {
        try {
            OutputStream out=socket.getOutputStream();
            PrintStream printStream=new PrintStream(out);
            printStream.println(message);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(String name) {
        this.name=name; //ClientHandler当前处理的客户端socket name
        socketMap.put(name,this.client);
        System.out.println(name+"注册到系统中");
        sendMessage(this.client,"欢迎:"+this.name+"注册成功");
        printOnlineClient();
    }

    private void groupChat(String message) {
    for(Map.Entry<String,Socket> entry : socketMap.entrySet()){
        Socket socket=entry.getValue();
        if(socket==this.client){
            continue;
        }
        sendMessage(socket,this.name+"说:"+message);
    }
    }
}

