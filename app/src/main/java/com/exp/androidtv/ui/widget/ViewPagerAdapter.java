package com.exp.androidtv.ui.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import com.example.core.view.FragmentPagerAdapter;
import com.exp.androidtv.ui.BaseFragment;
import java.util.List;

/**
 * Created by haiyu.chen on 2018/12/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
  public FragmentManager fragmentManager;
  public List<BaseFragment> list;

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
    super(fm);
    this.fragmentManager = fm;
    this.list = list;
  }

  @Override public Fragment getItem(int position) {
    return list.get(position);
  }

  @Override public int getCount() {
    return list != null ? list.size() : 0;
  }

  @Override public Fragment instantiateItem(ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
    return fragment;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    // super.destroyItem(container, position, object);
    Fragment fragment = list.get(position);
    fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
  }
}