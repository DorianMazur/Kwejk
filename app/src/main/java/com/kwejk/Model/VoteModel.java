package com.kwejk.Model;

import com.kwejk.Adapter.Adapter;

public class VoteModel {
    public Adapter.Holder holder;
    public String vote;
    public String id;
    public Integer result;

    public VoteModel(Adapter.Holder holder, String vote, String id){
        this.holder = holder;
        this.vote = vote;
        this.id = id;
    }

}
