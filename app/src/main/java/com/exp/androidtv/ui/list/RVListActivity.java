package com.exp.androidtv.ui.list;

import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.example.core.view.RecyclerViewTV;
import com.exp.androidtv.R;
import com.exp.androidtv.ui.BaseActivity;
import com.exp.androidtv.ui.widget.ListItemAdapter;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by haiyu.chen on 2018/12/17.
 * RecyclerViewTV 垂直列表
 */

public class RVListActivity extends BaseActivity {
  @BindView(R.id.rvt_item) RecyclerViewTV rvtItem;
  private ListItemAdapter listItemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_rv_list;
  }

  @Override protected void onInit() {
    listItemAdapter = new ListItemAdapter();
    rvtItem.setAdapter(listItemAdapter);
    rvtItem.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 20),
        AutoSizeUtils.pt2px(context, 0));
    rvtItem.setOnItemListener(new RecyclerViewTV.OnItemListener() {
      @Override public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setInterpolator(new BounceInterpolator())
            .setDuration(300)
            .start();
        itemView.setBackgroundResource(R.drawable.bg_item);
      }

      @Override public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setInterpolator(new BounceInterpolator())
            .setDuration(300)
            .start();
        itemView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
      }
    });
    listItemAdapter.setNewData(mockData.listItems);
  }
}
