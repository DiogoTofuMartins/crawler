package org.studies;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    private static ServerSocket socket;
    private static Crawler crawler;

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
        main.search(clientSocket);

    }

    public void search(Socket clientSocket) {

        String word = "";
        String link = "";
        BufferedOutputStream bufferedOutputStream;

        try {
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(clientSocket.getInputStream());
            bufferedOutputStream.write("Introduce key word to search: ".getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            word = scanner.nextLine();
            bufferedOutputStream.write(("Introduce site to search: ").getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            link = scanner.nextLine();

            if (link.contains("w3schools")) {
                crawler = new W3schoolCrawler(clientSocket, word);
            } else {
                crawler = new ScihubCrawler(clientSocket, word);
            }
            crawler.init();

            bufferedOutputStream.write(("Want to make another search? Y / N \n").getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();

            if (scanner.nextLine().equals("y")) {
                search(clientSocket);
            }

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
