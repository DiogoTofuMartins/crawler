package org.studies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Crawler {

    private List<String> visitedUrls;
    private String word;
    private String link;
    private Socket clientSocket;
    private BufferedOutputStream bufferedOutputStream;

    public Crawler(String word, String link){
        this.word = word;
        this.link = link;
        visitedUrls = new LinkedList<>();
    }

    public Crawler(Socket clientSocket){
        this.clientSocket = clientSocket;
        visitedUrls = new LinkedList<>();
        try {
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        try {
            Scanner scanner = new Scanner(clientSocket.getInputStream());
            bufferedOutputStream.write("Introduce key word to search: ".getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            word = scanner.nextLine();
            bufferedOutputStream.write(("Introduce link to search: ").getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            link = scanner.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        linkTitle(link);
    }

    public void linkTitle(String url){

        if(url.startsWith("/")){

            url = link + "/" + url;

        } else if (url.startsWith(word)) {

            url = link + "/" + word + "/" + url;

        } else if (url.equals(link)){


        } else return;

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            return;
        }

        if(!(visitedUrls.contains(url))) {
            visitedUrls.add(url);
            try {
                String defUrl = url + "\n";
                bufferedOutputStream.write(defUrl.getBytes(StandardCharsets.UTF_8));
                bufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else return;

        //get all links and recursively call the processPage method
        Elements questions = doc.select("a[href]");
        System.out.println(questions.size() + "          " + url);

        for(Element link: questions) {

            String newUrl = link.attr("href");
            System.out.println(newUrl);

            if (newUrl.contains(word) && !(visitedUrls.contains(newUrl)) && !(newUrl.contains("index.php"))) {

                linkTitle(link.attr("href"));
            }
        }
    }
}



