package com.wiatec.bplay.view.custom_view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by patrick on 28/09/2017.
 * create time : 3:42 PM
 */
public class TvGridLayoutManager extends GridLayoutManager {

    public TvGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TvGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public TvGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if (nextFocus == null) {
            return null;
        }
        int currentPosition = getPosition(focused);
        int nextPosition = getNextViewPosition(currentPosition, focusDirection);
        return findViewByPosition(nextPosition);
    }

    private int getNextViewPosition(int fromPos, int direction) {
        int offset = calcOffsetToNextView(direction);
        if (hitBorder(fromPos, offset)) {
            return fromPos;
        }
        return fromPos + offset;
    }

    private int calcOffsetToNextView(int direction) {
        int spanCount = getSpanCount();
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return spanCount;
                case View.FOCUS_UP:
                    return -spanCount;
                case View.FOCUS_RIGHT:
                    return 1;
                case View.FOCUS_LEFT:
                    return -1;
            }
        } else if (orientation == HORIZONTAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return 1;
                case View.FOCUS_UP:
                    return -1;
                case View.FOCUS_RIGHT:
                    return spanCount;
                case View.FOCUS_LEFT:
                    return -spanCount;
            }
        }

        return 0;
    }

    private boolean hitBorder(int from, int offset) {
        int spanCount = getSpanCount();

        if (Math.abs(offset) == 1) {
            int spanIndex = from % spanCount;
            int newSpanIndex = spanIndex + offset;
            return newSpanIndex < 0 || newSpanIndex >= spanCount;
        } else {
            int newPos = from + offset;
            return newPos < 0 && newPos >= spanCount;
        }
    }
}
