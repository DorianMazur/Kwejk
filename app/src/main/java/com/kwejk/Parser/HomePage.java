package com.kwejk.Parser;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import android.content.Intent;

import com.kwejk.Activity.HomeActivity;
import com.kwejk.Activity.MainActivity;
import com.kwejk.Model.MemeModel;


public class HomePage extends AsyncTask<String, Void, List<MemeModel>> {

    private ParserResponseInterface parserResponseInterface;
    private Integer currentPage = 0;
    private Integer maxPage = 0;

    public HomePage(ParserResponseInterface parserResponseInterface){
        this.parserResponseInterface = parserResponseInterface;
    }

    @Override
    protected List<MemeModel> doInBackground(String... params) {

        String url = params[0];
        List<MemeModel> memesList = new ArrayList<>();

        String title, src, votesUp, votesDown, id, comments;
        Integer page;

        Document pageDocument;
        Elements memes;

        try {
            pageDocument = Jsoup.connect(url).get();

            memes = pageDocument.select(".col-sm-8 .media-element");
            page = Integer.parseInt(pageDocument.select(".pagination ul .current").text());
            currentPage = page;
            if(url == "https://kwejk.pl" || url == "https://kwejk.pl/oczekujace" || url == "https://kwejk.pl/top/12h" || url == "https://kwejk.pl/top/24h" || url == "https://kwejk.pl/top/48h"){
                maxPage = page;
            }

            for(Element mem: memes) {
                if(!mem.select(".figure-holder figure a img").isEmpty()) {
                    if("false".equals(mem.attr("data-gallery"))){
                        Integer height = Integer.parseInt(mem.select(".figure-holder figure a img").attr("height"));
                        id = mem.attr("data-id");
                        if (height < 2400) {
                            title = mem.select(".content h2").text();
                            comments = mem.attr("data-comments-count");
                            src = mem.select(".figure-holder figure a img").attr("src");
                            votesUp = mem.attr("data-vote-up");
                            votesDown = mem.attr("data-vote-down");
                            memesList.add(new MemeModel(title, src, votesUp, votesDown, id, comments));
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return memesList;
    }

    @Override
    protected void onPostExecute(List<MemeModel> list) {
        parserResponseInterface.onParsingDone(list, currentPage, maxPage);
    }
}



