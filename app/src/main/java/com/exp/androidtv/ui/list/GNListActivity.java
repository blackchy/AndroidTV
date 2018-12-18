package com.exp.androidtv.ui.list;

import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.leanback.HorizontalRowView;
import com.example.core.leanback.OnItemListener;
import com.example.core.leanback.VerticalGridView;
import com.exp.androidtv.R;
import com.exp.androidtv.model.GNList;
import com.exp.androidtv.ui.BaseActivity;
import com.exp.androidtv.ui.widget.ListItemAdapter;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by haiyu.chen on 2018/12/17.
 * leanback 垂直列表嵌套水平列表
 */

public class GNListActivity extends BaseActivity {
  @BindView(R.id.vgv_item) VerticalGridView vgvItem;
  private ItemAdapter itemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_gn_list;
  }

  @Override protected void onInit() {
    itemAdapter = new ItemAdapter();
    vgvItem.setAdapter(itemAdapter);
    vgvItem.setVerticalSpacing(AutoSizeUtils.pt2px(context, 20));
    itemAdapter.setNewData(mockData.gnLists);
  }

  public class ItemAdapter extends BaseQuickAdapter<GNList, BaseViewHolder> {
    public ItemAdapter() {
      super(R.layout.layout_gn);
    }

    @Override protected void convert(BaseViewHolder helper, GNList item) {
      HorizontalRowView horizontalRowView = helper.getView(R.id.hrv_item);
      ListItemAdapter listItemAdapter = new ListItemAdapter();
      horizontalRowView.getGridView().setAdapter(listItemAdapter);
      horizontalRowView.getGridView().setHorizontalSpacing(AutoSizeUtils.pt2px(context, 20));
      horizontalRowView.getGridView().setOnItemListener(new OnItemListener() {
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
}
