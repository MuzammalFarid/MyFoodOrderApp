package com.example.myfoodorderapp.CustomFont;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class NativelyCustomTextView extends AppCompatTextView {
    public NativelyCustomTextView(Context context) {
        super(context);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

}