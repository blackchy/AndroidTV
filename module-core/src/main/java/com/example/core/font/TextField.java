package com.example.core.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.example.core.R;

/**
 */
@SuppressLint("AppCompatCustomView")
public class TextField extends TextView {
    public TextField(final Context context, final AttributeSet attrs) {
        super(context, attrs, R.attr.textFieldStyle);
    }
}
