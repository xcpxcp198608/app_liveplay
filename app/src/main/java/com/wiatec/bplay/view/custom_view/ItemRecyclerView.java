package com.wiatec.bplay.view.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by patrick on 25/07/2017.
 * create time : 5:03 PM
 */

public class ItemRecyclerView extends RecyclerView {

    public ItemRecyclerView(Context context) {
        this(context, null);
    }

    public ItemRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = super.dispatchKeyEvent(event);
        View focusView = this.findFocus();
        if (focusView == null) {
            return result;
        } else {
            int dy = 0;
            int dx = 0;
            if (getChildCount() > 0) {
                View firstView = this.getChildAt(0);
                dy = firstView.getHeight();
                dx = firstView.getWidth();
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT ||
                        event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT ||
                        event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN ||
                        event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP ) {
                    return true;
                }else {
                    return result;
                }
            } else {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                        if (rightView != null) {
                            rightView.requestFocus();
                            return true;
                        } else {
                            this.smoothScrollBy(dx, 0);
                            return true;
                        }
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                        if (leftView != null) {
                            leftView.requestFocus();
                            return true;
                        } else {
                            this.smoothScrollBy(-dx, 0);
                            return true;
                        }
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                        if (downView != null) {
                            downView.requestFocus();
                            return true;
                        } else {
                            this.smoothScrollBy(0, dy);
                            return true;
                        }
                    case KeyEvent.KEYCODE_DPAD_UP:
                        View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            return true;
                        } else {
                            if (upView != null) {
                                upView.requestFocus();
                                return true;
                            } else {
                                this.smoothScrollBy(0, -dy);
                                return true;
                            }
                        }
                    default:
                        return result;
                }
            }
        }
    }
}
