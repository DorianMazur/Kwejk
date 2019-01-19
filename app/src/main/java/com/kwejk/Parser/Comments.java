package com.kwejk.Parser;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import android.content.Intent;

import com.kwejk.Activity.HomeActivity;
import com.kwejk.Activity.MainActivity;
import com.kwejk.Model.CommentModel;


public class Comments extends AsyncTask<String, Void, List<CommentModel>> {

    private CommentsResponseInterface parserResponseInterface;

    public Comments(CommentsResponseInterface parserResponseInterface){
        this.parserResponseInterface = parserResponseInterface;
    }

    @Override
    protected List<CommentModel> doInBackground(String... params) {

        String id = params[0];
        List<CommentModel> commentsList = new ArrayList<>();
        Log.e("ArrayD", id);
        try {
            String response = Jsoup.connect("https://kwejk.pl/comment/get?id=" + id + "&mode=media&page=1&order=created-at&way=DESC")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute()
                    .body();
            try {
                JSONObject jsonObj = new JSONObject(response);

                Log.e("ArrayD", jsonObj.toString());
                JSONArray array = jsonObj.getJSONObject("comments").getJSONArray("data");
                for(int i = 0 ; i < array.length() ; i++){
                    String author = "Anonimowy";
                    if(array.getJSONObject(i).getString("user") != "null"){
                        author = array.getJSONObject(i).getJSONObject("user").getString("uacc_username");
                    }
                    String text = array.getJSONObject(i).getString("comment");
                    String date = array.getJSONObject(i).getString("created_at");
                    commentsList.add(new CommentModel(author, text, date, ""));
                }

            } catch (final JSONException e) {
                Log.e("ArrayD", "Json parsing error: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return commentsList;
    }

    @Override
    protected void onPostExecute(List<CommentModel> list) {
        parserResponseInterface.onParsingDone(list);
    }
}



