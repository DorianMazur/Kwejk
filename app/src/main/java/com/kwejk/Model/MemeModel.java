package com.kwejk.Model;

public class MemeModel {

    String title;
    String imageUrl;
    String votesUp;
    String votesDown;
    String dataId;
    String comments;

    public MemeModel(String title, String imageUrl, String votesUp, String votesDown, String dataId, String comments) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.dataId = dataId;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVotesUp() { return votesUp; }

    public String getVotesDown(){ return votesDown; }

    public String getId(){ return dataId; }

    public String getComments(){ return comments; }
}