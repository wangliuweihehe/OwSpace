package com.wlw.admin.owspace.view.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * @author admin
 */
public class FixedImageView extends AppCompatImageView {

    private int mScreenHeight;

    public FixedImageView(Context context) {
        this(context, null);
    }

    public FixedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mScreenHeight = getScreenWidthHeight(context)[1];
    }

    public static int[] getScreenWidthHeight(Context context) {
        int[] arrayOfInt = new int[2];
        if (context == null) {
            return arrayOfInt;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        arrayOfInt[0] = displayMetrics.widthPixels;
        arrayOfInt[1] = displayMetrics.heightPixels;
        return arrayOfInt;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(i, mScreenHeight);
    }
}
