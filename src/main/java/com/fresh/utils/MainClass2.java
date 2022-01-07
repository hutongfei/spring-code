package com.fresh.utils;

import cn.hutool.core.io.IoUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainClass2 {

    private static Object object = new Object();

    public static void main(String[] args) throws IOException, InterruptedException {
        serverV1();

    }

    private static void serverV2() {
        int port=4406;
        try {
            //创建服务端
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动，运行在"+serverSocket.getLocalSocketAddress());
            //等待客户端连接
            Socket clientSocket=serverSocket.accept();//此时阻塞，等待客户端连接  直到客户端连接服务端返回Socket
            System.out.println("有新用户连接，客户名为"+clientSocket.getPort());
            //数据接收和发送
            //1.接收
            InputStream in = clientSocket.getInputStream();
            Scanner scanner = new Scanner(in);
            System.out.println("客户端>" + scanner.nextLine());
            //2.发送
            OutputStream out = clientSocket.getOutputStream();
            PrintStream printStream = new PrintStream(out);
            printStream.println("你好，我是客户端==");
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void serverV1() throws IOException, InterruptedException {
        java.net.ServerSocket serverSocket = new java.net.ServerSocket(9090);

//        Socket accept = serverSocket.accept();
        System.out.println("Server:");

        while (true) {
            Socket accept = serverSocket.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("--------Out-------- ");
                        Scanner scanner = new Scanner(System.in);
                        String next = scanner.nextLine();
                        OutputStream os = accept.getOutputStream();
                        os.write(next.getBytes());
                        os.flush();
                        accept.shutdownOutput();
                        System.out.println("-------In--------- ");
                        InputStream is = accept.getInputStream();
                        iORead(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }

    private static void iORead(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String s;
        while ((s = in.readLine()) != null) {
            System.out.println(s);
        }
    }

}
