package com.exp.androidtv;

import android.app.Application;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * Created by haiyu.chen on 2018/12/14.
 */

public class AndroidTVApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    AutoSizeConfig.getInstance().getUnitsManager().setSupportDP(false).setSupportSubunits(Subunits.PT);
  }
}
