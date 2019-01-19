package com.kwejk.Listener;

import android.util.Log;
import android.widget.AbsListView;

import com.kwejk.Activity.HomeActivity;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    public static int state = 0;

    public EndlessScrollListener() {
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

        HomeActivity.toolbar.setSubtitle("Obrazek " + String.valueOf(firstVisibleItem + 1) + "/" + String.valueOf(totalItemCount));
        if(totalItemCount != 0) {
            if (state == 0 && (totalItemCount - firstVisibleItem) < 4) {
                this.previousTotalItemCount = totalItemCount;
                this.state = 1;
            }
        }

        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.

        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (state == 1) {
            state = 2;
            onLoadMore(totalItemCount);
        }
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract void onLoadMore(int totalItemsCount);


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
