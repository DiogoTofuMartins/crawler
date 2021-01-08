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

public class W3schoolCrawler implements Crawler{

    private List<String> visitedUrls;
    private String word;
    private String link = "http://www.w3schools.com";
    private Socket clientSocket;
    private BufferedOutputStream bufferedOutputStream;

    public W3schoolCrawler(Socket clientSocket, String word){
        this.clientSocket = clientSocket;
        this.word = word;
        visitedUrls = new LinkedList<>();
        try {
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(){

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

        for(Element link: questions) {

            String newUrl = link.attr("href");

            if (newUrl.contains(word) && !(visitedUrls.contains(newUrl)) && !(newUrl.contains("index.php"))) {

                linkTitle(link.attr("href"));
            }
        }
    }
}



