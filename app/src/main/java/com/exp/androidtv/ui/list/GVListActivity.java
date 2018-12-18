package com.exp.androidtv.ui.list;

import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.example.core.leanback.OnItemListener;
import com.example.core.leanback.VerticalGridView;
import com.exp.androidtv.R;
import com.exp.androidtv.ui.BaseActivity;
import com.exp.androidtv.ui.widget.ListItemAdapter;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by haiyu.chen on 2018/12/14.
 * leanback 垂直列表
 */

public class GVListActivity extends BaseActivity {
  @BindView(R.id.vgv_item) VerticalGridView vgvItem;
  private ListItemAdapter listItemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_gv_list;
  }

  @Override protected void onInit() {
    listItemAdapter = new ListItemAdapter();
    vgvItem.setAdapter(listItemAdapter);
    vgvItem.setVerticalSpacing(AutoSizeUtils.pt2px(context, 20));
    vgvItem.setOnItemListener(new OnItemListener() {
      @Override public void onItemSelected(Object parent, View itemView, int position) {
        itemView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setInterpolator(new BounceInterpolator())
            .setDuration(300)
            .start();
        itemView.setBackgroundResource(R.drawable.bg_item);
      }

      @Override public void onItemPreSelected(Object parent, View itemView, int position) {
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
