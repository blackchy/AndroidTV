package com.exp.androidtv.navigation;

import android.content.Context;
import android.content.Intent;
import com.exp.androidtv.ui.list.GHListActivity;
import com.exp.androidtv.ui.list.GNListActivity;
import com.exp.androidtv.ui.list.GVListActivity;
import com.exp.androidtv.ui.list.RGListActivity;
import com.exp.androidtv.ui.list.RHListActivity;
import com.exp.androidtv.ui.list.RNListActivity;
import com.exp.androidtv.ui.list.RSListActivity;
import com.exp.androidtv.ui.list.RVListActivity;
import com.exp.androidtv.ui.page.OutFrameActivity;

public final class Navigator {
  private static volatile Navigator instance = null;

  private Navigator() {
  }

  public static Navigator getInstance() {
    if (instance == null) {
      synchronized (Navigator.class) {
        if (instance == null) {
          instance = new Navigator();
        }
      }
    }
    return instance;
  }

  public void navToGVList(Context context) {
    context.startActivity(new Intent(context, GVListActivity.class));
  }

  public void navToGHList(Context context) {
    context.startActivity(new Intent(context, GHListActivity.class));
  }

  public void navToGNList(Context context) {
    context.startActivity(new Intent(context, GNListActivity.class));
  }

  public void navToRHList(Context context) {
    context.startActivity(new Intent(context, RHListActivity.class));
  }

  public void navToRVList(Context context) {
    context.startActivity(new Intent(context, RVListActivity.class));
  }

  public void navToRGList(Context context) {
    context.startActivity(new Intent(context, RGListActivity.class));
  }

  public void navToRNList(Context context) {
    context.startActivity(new Intent(context, RNListActivity.class));
  }

  public void navToRSList(Context context) {
    context.startActivity(new Intent(context, RSListActivity.class));
  }

  public void navToOutFrame(Context context) {
    context.startActivity(new Intent(context, OutFrameActivity.class));
  }
}
