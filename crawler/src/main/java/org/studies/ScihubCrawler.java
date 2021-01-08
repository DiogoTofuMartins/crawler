package org.studies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ScihubCrawler implements Crawler {

    private Map<String,String> visitedUrls;
    private Socket clientSocket;
    private BufferedOutputStream bufferedOutputStream;
    private String word;
    private String link = "https://citationsy.com/archives/search.php?CitationsyArchives_search=" ;

    public ScihubCrawler(Socket clientSocket, String word){
        this.clientSocket = clientSocket;
        this.word = word;
        this.visitedUrls = new HashMap<>();
        try {
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void init() throws IOException {

        linkTitle(link + word);

    }

    public void linkTitle(String url) throws IOException {

        if(visitedUrls.containsKey(url)){
            return;
        }
        System.out.println(url);
        Document doc = null;


        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            return;
        }

        //get all links and recursively call the processPage method
        Elements questions = doc.select("a[href]");

        for(Element link: questions) {
            // System.out.println(link);
            String newUrl = link.attr("href");

            if (newUrl.startsWith("q?doi")) {
                newUrl = "https://citationsy.com/archives/" + newUrl;
                Elements texts = doc.select("div[class]");

                for(Element text : texts){
                    if(text.attr("class").equals("CitationsyArchives_search_result") && !(visitedUrls.containsValue(text.text()))){

                        visitedUrls.put(newUrl,text.text());
                        bufferedOutputStream.write((newUrl + " :\n " + text.text() + "\n").getBytes(StandardCharsets.UTF_8));
                        bufferedOutputStream.flush();
                        break;
                    }
                }
                linkTitle(newUrl);
            }
        }
    }
}
