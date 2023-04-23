package org.example;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.HashSet;

public class Crawler {

    HashSet<String> urlSet;


    // set a depth limit

    int maxDepth = 2;


    Crawler(){
        urlSet = new HashSet<>();
    }

    // create recursive function ie crawler bot

    public void getPageTextsAndLinks(String url, int depth){

        if(urlSet.contains(url)){
            return;
        }
        if(depth >=maxDepth){
            return;
        }
        if(urlSet.add(url)){
            System.out.println(url);
        }

        depth++;

        try{

            // parse the html object to java object

            Document document = Jsoup.connect(url).timeout(5000).get();

            System.out.println(document.title());

            // indexer work  starts here
            // it saves the data to database
            Indexer indexer = new Indexer(document,url);



            Elements  availableLinksonPage = document.select("a[href]");

            for(Element currentLink : availableLinksonPage){
                getPageTextsAndLinks(currentLink.attr("abs:href"),depth);
            }

        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }

    }
    public static void main(String[] args) {


         Crawler crawler = new Crawler();

         crawler.getPageTextsAndLinks("https://www.javatpoint.com",0);


    }
}