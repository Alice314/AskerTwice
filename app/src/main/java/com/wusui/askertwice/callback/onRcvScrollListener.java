package com.wusui.askertwice.callback;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.view.View;

/**
 * Created by fg on 2016/7/28.
 */

public class onRcvScrollListener extends RecyclerView.OnScrollListener implements onBottomListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            int lastPosition = -1;

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {

                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);
                }

                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    onBottom();
                }

            }
        }
// View
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
            int lastChildBottom = lastChildView.getBottom();
            int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
            int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
            if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                onBottom();
            }
        }

        @Override
        public void onBottom() {
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
}
