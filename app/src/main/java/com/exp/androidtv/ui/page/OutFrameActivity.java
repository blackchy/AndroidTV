package com.exp.androidtv.ui.page;

import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import butterknife.BindColor;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.view.FixedSpeedScroller;
import com.example.core.view.RecyclerViewTV;
import com.example.core.view.VerticalViewPager;
import com.exp.androidtv.R;
import com.exp.androidtv.ui.BaseActivity;
import com.exp.androidtv.ui.BaseFragment;
import com.exp.androidtv.ui.widget.ViewPagerAdapter;
import java.lang.reflect.Field;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by haiyu.chen on 2018/12/18.
 */

public class OutFrameActivity extends BaseActivity {
  @BindView(R.id.rvt_menu) RecyclerViewTV rvtMenu;
  @BindView(R.id.vvp_content) VerticalViewPager vvpContent;
  @BindColor(android.R.color.transparent) int transparent;
  @BindColor(R.color.colorPrimary) int darkerGray;
  @BindColor(android.R.color.tertiary_text_light) int textLight;
  private MenuAdapter menuAdapter;
  private boolean isRight = false;
  private ViewPagerAdapter pagerAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_out_frame;
  }

  @Override protected void onInit() {
    menuAdapter = new MenuAdapter();
    rvtMenu.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 20),
        AutoSizeUtils.pt2px(context, 0));
    rvtMenu.setAdapter(menuAdapter);
    rvtMenu.setOnItemListener(new RecyclerViewTV.OnItemListener() {
      @Override public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        if (position != vvpContent.getCurrentItem()) {
          vvpContent.setCurrentItem(position);
        }
        itemView.setBackgroundColor(darkerGray);
      }

      @Override public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.setBackgroundColor(isRight ? textLight : transparent);
      }
    });
    rvtMenu.setOnChildViewHolderSelectedListener(
        new RecyclerViewTV.OnChildViewHolderSelectedListener() {
          @Override
          public void onChildViewHolderSelected(RecyclerViewTV parent, RecyclerView.ViewHolder vh,
              int position) {
            vh.itemView.setOnKeyListener(new View.OnKeyListener() {
              @Override public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                  isRight = i == KeyEvent.KEYCODE_DPAD_RIGHT;
                }
                return false;
              }
            });
          }
        });
    menuAdapter.setNewData(mockData.outMenu);
    for (int i = 0; i < mockData.outMenu.size(); i++) {
      BaseFragment fragment = new ConentFragment();
      Bundle bundle = new Bundle();
      bundle.putInt("menuIndex", i);
      fragment.setArguments(bundle);
      fragments.add(fragment);
    }
    pagerAdapter = new ViewPagerAdapter(fragmentManager, fragments);
    vvpContent.setAdapter(pagerAdapter);
    setViewPagerSpeed(vvpContent, 300);//设置viewpager切换的速度
  }

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    rvtMenu.setSelection(0);
  }

  protected void setViewPagerSpeed(VerticalViewPager viewPager, int mDuration) {
    try {
      Field field = VerticalViewPager.class.getDeclaredField("mScroller");
      field.setAccessible(true);
      FixedSpeedScroller scroller =
          new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
      field.set(viewPager, scroller);
      scroller.setmDuration(mDuration);
    } catch (Exception e) {
    }
  }

  public class MenuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MenuAdapter() {
      super(R.layout.item_menu);
    }

    @Override protected void convert(BaseViewHolder helper, String item) {
      helper.setText(R.id.tv_menu, item);
    }
  }
}
