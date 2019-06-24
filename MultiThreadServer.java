package com.zh.server.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    public static void main(String[] args) {
        int defaultPort=5506;
        int port=defaultPort;
        for(String arg:args) {                      //使用命令行参数来实现自行输入端口号
            if (arg.startsWith("--port=")) {
                String portstr = arg.substring("--port=".length());
                try {
                    port = Integer.parseInt(portstr);   //将其转换为整数
                } catch (NumberFormatException e) {
                    port = defaultPort;      //若输入不符合就使用默认的
                }
            }
        }
        final ExecutorService executorService= Executors.newFixedThreadPool(2*Runtime.getRuntime().availableProcessors());
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("服务端启动，运行在: "+serverSocket.getLocalSocketAddress());
            //接收，循环
            while(true){
                final Socket socket=serverSocket.accept();
                System.out.println("客户端连接，来自："+socket.getRemoteSocketAddress());//连接远程的地址
                executorService.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
    }
}
