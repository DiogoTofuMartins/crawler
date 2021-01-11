package org.studies;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Main {

    private ServerSocket socket;
    private Crawler crawler;
    private String[] options = {"W3schools", "Tutorialspoint", "Scihub"};
    private Prompt prompt;
    private MenuInputScanner menuInputScanner;
    private Map<Integer, Crawler> crawlerMap;
    private StringInputScanner scanner;

    public Main(int port){

        crawlerMap = new HashMap<>();
        crawlerMap.put(1, new W3schoolCrawler());
        crawlerMap.put(2, new TutorialsCrawler());
        crawlerMap.put(3, new ScihubCrawler());
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

        try {
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            menuInputScanner = new MenuInputScanner(options);
            scanner = new StringInputScanner();

            menuInputScanner.setMessage("Which site you would like to search in? ");
            crawler = crawlerMap.get(prompt.getUserInput(menuInputScanner));

            scanner.setMessage("What concept would you like to search?\n");
            word = prompt.getUserInput(scanner);

            crawler.setClientSocket(clientSocket);
            crawler.setPrompt(prompt);
            crawler.setWord(word);

            crawler.init();

            menuInputScanner = new MenuInputScanner(new String[]{"yes", "no"});
            menuInputScanner.setMessage("Want to make another search?");

            if (prompt.getUserInput(menuInputScanner) == 1) {
                search(clientSocket);
            }

            clientSocket.close();
            socket.close();
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
