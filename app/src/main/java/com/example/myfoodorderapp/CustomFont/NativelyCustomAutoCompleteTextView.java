package com.example.myfoodorderapp.CustomFont;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

/**
 * Created by Muhammad Mubashir on 6/21/2017.
 */

public class NativelyCustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    public NativelyCustomAutoCompleteTextView(Context context) {
        super(context);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

    public NativelyCustomAutoCompleteTextView(Context context, AttributeSet attrs,
                                              int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(scairoregular.getInstance(context).getTypeFace());
    }

}