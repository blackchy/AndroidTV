package com.exp.androidtv.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.core.utils.AppManager;
import com.example.core.utils.SmartToast;
import com.exp.androidtv.data.MockData;
import com.exp.androidtv.navigation.Navigator;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends FragmentActivity {
  private CompositeDisposable mCompositeDisposable;
  protected SmartToast toastor;
  protected Navigator navigator;
  private Unbinder unbinder;
  private AppManager manager;
  protected MockData mockData;
  protected Context context;

  protected List<BaseFragment> fragments = new ArrayList<>();
  protected FragmentManager fragmentManager = getSupportFragmentManager();
  protected int showIndex = -2;
  protected static final int LEFT = 0;
  protected static final int RIGHT = 1;
  protected static final int UP = 3;
  protected static final int DOWN = 4;
  protected static final int NONE = -1;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    manager = AppManager.getAppManager();
    toastor = new SmartToast(this);
    navigator = Navigator.getInstance();
    manager.addActivity(this);
    setContentView(layoutResId());
    unbinder = ButterKnife.bind(this);
    mockData = MockData.getInstance();
    mCompositeDisposable = new CompositeDisposable();
    onInit();
  }

  protected abstract void onInit();

  protected abstract int layoutResId();

  @Override protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
    manager.finishActivity(this);
    clearDisposable();
  }

  protected void addDisposable(Disposable mDisposable) {
    if (mCompositeDisposable == null) {
      mCompositeDisposable = new CompositeDisposable();
    }
    mCompositeDisposable.add(mDisposable);
  }

  private void clearDisposable() {
    if (mCompositeDisposable != null) {
      mCompositeDisposable.clear();
    }
  }

  protected void exit() {
    manager.appExit(this);
  }
}
