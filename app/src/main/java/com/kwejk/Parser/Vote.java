package com.kwejk.Parser;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import android.content.Intent;

import com.kwejk.Model.VoteModel;


public class Vote extends AsyncTask<VoteModel, Void, VoteModel> {

    @Override
    protected VoteModel doInBackground(VoteModel... params) {

        VoteModel voteModel = params[0];

        try {
            Document doc = Jsoup.connect("https://kwejk.pl/media/vote")
                    .data("id", voteModel.id)
                    .data("vote", voteModel.vote)
                    .ignoreContentType(true)
                    .post();

            JSONObject jObject = new JSONObject(doc.body().text());
            voteModel.result = Integer.parseInt(jObject.getString("result"));

        } catch (Exception e) {
            e.printStackTrace();
        }
       return voteModel;
    }

    @Override
    protected void onPostExecute(VoteModel voteModel) {
        Log.e("VOTE RESULT:", voteModel.result.toString());
        if (voteModel.result == 1) {
            Integer newVotes = voteModel.holder.getVotesUp() + 1;
            voteModel.holder.setVotesUp(newVotes.toString());
        }
        if (voteModel.result == -1) {
            Integer newVotes = voteModel.holder.getVotesDown() + 1;
            voteModel.holder.setVotesDown(newVotes.toString());
        }
    }

}



