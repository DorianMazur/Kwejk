package com.kwejk.Parser;

import com.kwejk.Model.CommentModel;

import java.util.List;

public interface CommentsResponseInterface {

    void onParsingDone(List<CommentModel> list);

}
