package com.example.myfoodorderapp.CustomFont;

import android.content.Context;
import android.graphics.Typeface;

public class scairoregular {
    private static scairoregular instance;
    private static Typeface typeface;

    public static scairoregular getInstance(Context context) {
        synchronized (scairoregular.class) {
            if (instance == null) {
                instance = new scairoregular();
                typeface = Typeface.createFromAsset(context.getResources().getAssets(), "cairo_regular.ttf");
            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}