package com.mythicaljourneyman.tic_tac_toe.views.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mythicaljourneyman.tic_tac_toe.R;

/**
 * Created by Prabodh Dhabaria on 27-05-2018.
 */
public class IconTextView extends AppCompatTextView {
    public IconTextView(Context context) {
        super(context);
        setFont(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        Typeface tf = ResourcesCompat.getFont(context, R.font.icons);
        setTypeface(tf);
    }
}
