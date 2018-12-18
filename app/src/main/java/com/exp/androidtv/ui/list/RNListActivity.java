package com.exp.androidtv.ui.list;

import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.view.RecyclerViewTV;
import com.exp.androidtv.R;
import com.exp.androidtv.model.RNList;
import com.exp.androidtv.ui.BaseActivity;
import com.exp.androidtv.ui.widget.ListItemAdapter;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by haiyu.chen on 2018/12/17.
 * RecyclerViewTV 垂直列表嵌套水平列表
 */

public class RNListActivity extends BaseActivity {
  @BindView(R.id.rvt_item) RecyclerViewTV rvtItem;
  private ItemAdapter itemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_rn_list;
  }

  @Override protected void onInit() {
    itemAdapter = new ItemAdapter();
    rvtItem.setAdapter(itemAdapter);
    rvtItem.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 20),
        AutoSizeUtils.pt2px(context, 0));
    itemAdapter.setNewData(mockData.rnLists);
  }

  public class ItemAdapter extends BaseQuickAdapter<RNList, BaseViewHolder> {
    public ItemAdapter() {
      super(R.layout.layout_rn);
    }

    @Override protected void convert(BaseViewHolder helper, RNList item) {
      RecyclerViewTV rvthItem = helper.getView(R.id.rvt_h_item);
      ListItemAdapter listItemAdapter = new ListItemAdapter();
      rvthItem.setAdapter(listItemAdapter);
      rvthItem.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 0),
          AutoSizeUtils.pt2px(context, 20));
      rvthItem.setOnItemListener(new RecyclerViewTV.OnItemListener() {
        @Override public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
          itemView.animate()
              .scaleX(1.05f)
              .scaleY(1.05f)
              .setInterpolator(new BounceInterpolator())
              .setDuration(300)
              .start();
          itemView.setBackgroundResource(R.drawable.bg_item);
        }

        @Override
        public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
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
