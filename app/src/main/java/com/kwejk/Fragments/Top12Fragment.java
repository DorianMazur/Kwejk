package com.kwejk.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.*;

import com.kwejk.Activity.HomeActivity;
import com.kwejk.Listener.EndlessScrollListener;
import com.kwejk.Model.CommentModel;
import com.kwejk.R;
import com.kwejk.Parser.ParserResponseInterface;
import com.kwejk.Model.MemeModel;
import com.kwejk.Parser.HomePage;
import com.kwejk.Adapter.Adapter;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class Top12Fragment extends Fragment implements ParserResponseInterface {
    private List<MemeModel> memeList = new ArrayList<>();
    private Integer currentPage = 1;
    private Integer maxPage = 0;
    Adapter adapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new HomePage(this).execute("https://kwejk.pl/top/12h");
        return inflater.inflate(R.layout.home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.listView);
        adapter = new Adapter(getActivity(), R.layout.home, memeList);
        listView.setAdapter(adapter);
        HomeActivity.toolbar.setTitle("Najlepsze z 12h");
        ListView listView = (ListView) getView().findViewById(R.id.listView);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int totalItemsCount) {
                if(maxPage > currentPage) {
                    new HomePage(Top12Fragment.this).execute("https://kwejk.pl/top/12h/strona/" + (maxPage - currentPage));
                    currentPage++;
                }
            }
        });
    }

    @Override
    public void onParsingDone(List<MemeModel> memeList, Integer curP, Integer maxP){
        if(maxPage == 0) {
            this.maxPage = maxP;
            getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
        }
        for(int i = 0; i < memeList.size(); i++){
            this.memeList.add(memeList.get(i));
        }
        adapter.notifyDataSetChanged();
        EndlessScrollListener.state = 0;
    }


}
