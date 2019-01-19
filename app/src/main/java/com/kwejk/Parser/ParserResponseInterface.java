package com.kwejk.Parser;

import com.kwejk.Model.CommentModel;
import com.kwejk.Model.MemeModel;
import java.util.List;

public interface ParserResponseInterface {

    void onParsingDone(List<MemeModel> list, Integer cP, Integer mP);

}
