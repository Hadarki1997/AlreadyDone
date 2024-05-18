package com.example.alreadydone;

import android.text.InputFilter;
import android.text.Spanned;

public class HebrewInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!Character.toString(source.charAt(i)).matches("[א-ת ]+")) {
                return "";
            }
        }
        return null;
    }
}
