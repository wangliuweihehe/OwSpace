package com.wlw.admin.owspace.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SelectTextView extends AppCompatTextView {
    public SelectTextView(Context context) {
        super(context);
    }

    public SelectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (selectionStart < 0 || selectionEnd < 0) {
            Selection.setSelection((Spannable) getText(), getText().length());
        } else if (selectionStart != selectionEnd) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                final CharSequence text = getText();
                setText(null);
                setText(text);
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
