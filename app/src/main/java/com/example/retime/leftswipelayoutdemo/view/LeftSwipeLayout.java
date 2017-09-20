package com.example.retime.leftswipelayoutdemo.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by retime on 2017/9/14.
 */

public class LeftSwipeLayout extends LinearLayout {

    public static final int STATE_CLOSED = 0;
    public static final int STATE_OPEN = 1;
    public static final int STATE_SCROLL = 2;

    private int state = STATE_CLOSED;

    private Scroller mScroller;

    public LeftSwipeLayout(Context context) {
        this(context, null);
    }

    public LeftSwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);
        if (getChildCount() == 2 && getOrientation() == HORIZONTAL) {
            View mainLayout = getChildAt(0);
            LayoutParams mainLayoutParams = (LayoutParams) mainLayout.getLayoutParams();
            if (mainLayoutParams != null) {
                mainLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                mainLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    private PointF downPoint = new PointF();
    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getChildCount() != 2 || getOrientation() != HORIZONTAL) {
            return false;
        }
//        View mainLayout = getChildAt(0);
        View rightLayout = getChildAt(1);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.set(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (state == STATE_SCROLL) {
                    float offsetX = event.getX() - lastX;
                    //滑动逻辑
                    if ((getScrollX() >= rightLayout.getWidth() && offsetX <= 0)
                            || (getScrollX() <= 0 && offsetX >= 0)) {
                        return false;
                    }
                    int scrollToX = (int) (getScrollX() - offsetX);
                    if (scrollToX < 0) {
                        scrollToX = 0;
                    } else if (scrollToX > rightLayout.getWidth()) {
                        scrollToX = rightLayout.getWidth();
                    }
                    scrollTo(scrollToX, 0);
                    lastX = event.getX();
                    return true;
                }
                float offsetXAbs = Math.abs(event.getX() - downPoint.x);
                float offsetYAbs = Math.abs(event.getY() - downPoint.y);
                float startScrollOffset = 10;
                if (offsetXAbs > startScrollOffset || offsetYAbs > startScrollOffset) { //滑动超过一定距离
                    if (offsetXAbs > offsetYAbs) { //x轴偏移大于y轴偏移
                        state = STATE_SCROLL;
                        lastX = event.getX();
                        return true;
                    } else {
                        return false;
                    }
                } else { //滑动还没到一定距离的情况下，返回true
                    return true;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (state == STATE_SCROLL) {
                    //复位逻辑，展开或关闭
                    if (getScrollX() < rightLayout.getWidth() / 2) {
//                        scrollTo(0, 0);
                        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                        state = STATE_CLOSED;
                    } else {
//                        scrollTo(rightLayout.getWidth(), 0);
                        mScroller.startScroll(getScrollX(), 0, rightLayout.getWidth() - getScrollX(), 0);
                        state = STATE_OPEN;
                    }
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }
}
