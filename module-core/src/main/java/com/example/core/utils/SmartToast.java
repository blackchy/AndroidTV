package com.example.core.utils;

import android.content.Context;
import android.widget.Toast;

public class SmartToast {
  private Toast mToast;
  private Context mContext;

  public SmartToast(Context context) {
    this.mContext = context;
  }

  public void showToast(String text) {
    if (this.mToast == null) {
      this.mToast = Toast.makeText(this.mContext, text, Toast.LENGTH_LONG);
    } else {
      this.mToast.setText(text);
    }

    this.mToast.show();
  }
}
