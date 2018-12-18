package com.exp.androidtv.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.core.utils.SmartToast;
import com.exp.androidtv.data.MockData;
import com.exp.androidtv.navigation.Navigator;

public abstract class BaseFragment extends BaseAbsFragment {
  private View rootView;
  protected SmartToast toastor;
  protected Navigator navigator;
  private Unbinder unbinder;
  protected MockData mockData;
  protected Context mContext;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(layoutResId(), container, false);
    toastor = new SmartToast(mContext);
    navigator = Navigator.getInstance();
    unbinder = ButterKnife.bind(this, rootView);
    mockData = MockData.getInstance();
    initView();
    return rootView;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Override public void onDestroyView() {
    if (unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroyView();
  }

  @Override protected void onFragmentFirstVisible() {
    super.onFragmentFirstVisible();
    initData();
  }

  protected abstract void initView();

  protected abstract void initData();

  protected abstract int layoutResId();
}
