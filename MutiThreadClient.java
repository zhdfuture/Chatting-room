package com.zh.client.client;

import java.io.IOException;
import java.net.Socket;

public class MutiThreadClient {
    public static void main(String[] args) {
        String defaulthost="127.0.0.1";
        int defaultport=5506;
        String host=defaulthost;
        int port=defaultport;
        for(String arg:args){
            if(arg.startsWith("--port=")){
                String portstr=arg.substring(("--port=".length()));
                try{
                    port=Integer.parseInt(portstr);
                }catch(NumberFormatException e){
                    port=defaultport;
                }
            }
            if(arg.startsWith("--host=")){
                String hoststr=arg.substring("--host=".length());
                host=hoststr;
            }
        }
        try {
         final   Socket socket=new Socket(host,port);    //socket不能变，一旦创建好就不能再重新赋值，防止不对等
            //发数据
            new WriteDataToServerThread(socket).start();
            //收数据
            new ReadDataToServerThread(socket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
