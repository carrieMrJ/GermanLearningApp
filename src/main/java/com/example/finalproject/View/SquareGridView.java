package com.example.finalproject.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *Grid View for Calendar in daily attendance activity
 * @author Mengru.Ji
 */

public class SquareGridView extends GridView {
    /**
     *
     * @param context
     */
    public SquareGridView(Context context) {
        super(context);
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public SquareGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SquareGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * onMeasure
     * Measure GridView
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec =  MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}