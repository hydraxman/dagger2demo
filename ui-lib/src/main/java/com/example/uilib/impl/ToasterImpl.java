package com.example.uilib.impl;

import android.content.Context;
import android.widget.Toast;

import com.example.uilib.Toaster;

public class ToasterImpl implements Toaster {
    private Context applicationContext;

    public ToasterImpl(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void longToast(CharSequence text) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
    }
}
