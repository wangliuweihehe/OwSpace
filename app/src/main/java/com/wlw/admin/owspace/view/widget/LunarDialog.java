package com.wlw.admin.owspace.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wlw.admin.owspace.R;

public class LunarDialog extends Dialog {
    private Context context;

    public LunarDialog(@NonNull Context context) {
        super(context,R.style.LunarDialog);
        this.context = context;
        setCanceledOnTouchOutside(true);
    }

}
