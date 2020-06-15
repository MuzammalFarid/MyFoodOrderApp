package com.example.myfoodorderapp.CustomFont;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Muhammad Mubashir on 6/21/2017.
 */

public class NativelyCustomEditText extends AppCompatEditText {
    public NativelyCustomEditText(Context context) {
        super(context);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomEditText(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

}