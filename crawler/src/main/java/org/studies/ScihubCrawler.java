package org.studies;

import org.academiadecodigo.bootcamp.Prompt;
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
    private Prompt prompt;
    private String word;
    private String link = "https://citationsy.com/archives/search.php?CitationsyArchives_search=" ;

    public ScihubCrawler(){

        this.visitedUrls = new HashMap<>();

    }

    public void init(){

        linkTitle(link + word);

    }

    public void linkTitle(String url){

        if(visitedUrls.containsKey(url)){
            return;
        }

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
                        prompt.sendUserMsg(newUrl + " :\n " + text.text() + "\n");
                        break;
                    }
                }
                linkTitle(newUrl);
            }
        }
    }

    @Override
    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }

    @Override
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void setWord(String word) {
        this.word = word;
    }
}
