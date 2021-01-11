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
import java.util.LinkedList;
import java.util.List;

public class TutorialsCrawler implements Crawler{

    private List<String> visitedUrls;
    private String word;
    private String link = "https://www.tutorialspoint.com";
    private Socket clientSocket;
    private Prompt prompt;

    public TutorialsCrawler(){

        visitedUrls = new LinkedList<>();

    }

    public void init(){

        linkTitle(link);
    }

    public void linkTitle(String url){


        if(url.startsWith("/")){

            url = link + "/" + url;

        } else if (url.startsWith(word)) {

            url = link + "/" + word + "/" + url;

        } else if (url.startsWith(link)){


        } else return;

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            return;
        }

        if(!(visitedUrls.contains(url))) {

            visitedUrls.add(url);

                String defUrl = url + "\n";
                prompt.sendUserMsg(defUrl);

        } else return;



        //get all links and recursively call the processPage method
        Elements questions = doc.select("a[href]");

        for(Element link: questions) {

            String newUrl = link.attr("href");

            if (newUrl.contains(word) && !(visitedUrls.contains(newUrl)) && !(newUrl.contains("index.php"))) {

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
