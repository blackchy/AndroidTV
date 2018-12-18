package com.exp.androidtv;

import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.view.RecyclerViewTV;
import com.example.core.view.V7GridLayoutManager;
import com.exp.androidtv.ui.BaseActivity;
import java.util.List;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MainActivity extends BaseActivity {

  @BindView(R.id.rvt_item) RecyclerViewTV rvtItem;
  private ItemAdapter itemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_main;
  }

  @Override protected void onInit() {
    itemAdapter = new ItemAdapter(mockData.mainItems);
    itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
          case 0:
            navigator.navToGHList(context);
            break;
          case 1:
            navigator.navToGVList(context);
            break;
          case 2:
            navigator.navToGNList(context);
            break;
          case 3:
            navigator.navToRHList(context);
            break;
          case 4:
            navigator.navToRVList(context);
            break;
          case 5:
            navigator.navToRNList(context);
            break;
          case 6:
            navigator.navToRGList(context);
            break;
          case 7:
            navigator.navToRSList(context);
            break;
          case 8:
            navigator.navToOutFrame(context);
            break;
        }
      }
    });
    rvtItem.setAdapter(itemAdapter);
    rvtItem.setLayoutManager(new V7GridLayoutManager(context, 3));
    rvtItem.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 20),
        AutoSizeUtils.pt2px(context, 20));
    rvtItem.setOnItemListener(new RecyclerViewTV.OnItemListener() {
      @Override public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setInterpolator(new BounceInterpolator())
            .setDuration(300)
            .start();
      }

      @Override public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setInterpolator(new BounceInterpolator())
            .setDuration(300)
            .start();
      }
    });
  }

  private class ItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ItemAdapter(List<String> data) {
      super(R.layout.item_main, data);
    }

    @Override protected void convert(BaseViewHolder helper, String item) {
      helper.setText(R.id.tv_item_main, item);
    }
  }
}