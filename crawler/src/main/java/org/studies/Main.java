package org.studies;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static ServerSocket socket;

    public Main(int port){
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Main main = new Main(1500);
        Socket clientSocket = main.connect();
        Crawler crawler = new Crawler(clientSocket);
        crawler.init();

        try {
            clientSocket.close();
            Main.socket.close();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket connect(){
        try {
            return socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
