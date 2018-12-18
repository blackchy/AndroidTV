package com.exp.androidtv.ui.list;

import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.core.view.RecyclerViewTV;
import com.example.core.view.V7GridLayoutManager;
import com.exp.androidtv.R;
import com.exp.androidtv.model.RSModel;
import com.exp.androidtv.ui.BaseActivity;
import java.util.ArrayList;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.exp.androidtv.model.RSModel.RS_TYPE_IMAGE;
import static com.exp.androidtv.model.RSModel.RS_TYPE_LONG;
import static com.exp.androidtv.model.RSModel.RS_TYPE_NORMAL;

/**
 * Created by haiyu.chen on 2018/12/17.
 * RecyclerViewTV 同一个列表不相同的item
 */

public class RSListActivity extends BaseActivity {
  @BindView(R.id.rvt_item) RecyclerViewTV rvtItem;
  private ItemAdapter itemAdapter;

  @Override protected int layoutResId() {
    return R.layout.activity_rs_list;
  }

  @Override protected void onInit() {
    itemAdapter = new ItemAdapter();
    itemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
      @Override public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return RS_TYPE_LONG == mockData.rsModels.get(position).getItemType() ? 2 : 1;
      }
    });
    rvtItem.setSpacingWithMargins(AutoSizeUtils.pt2px(context, 20),
        AutoSizeUtils.pt2px(context, 20));
    rvtItem.setLayoutManager(new V7GridLayoutManager(context, 3));
    rvtItem.setAdapter(itemAdapter);
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
    itemAdapter.setNewData(mockData.rsModels);
    itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toastor.showToast("click" + position);
      }
    });
  }

  public class ItemAdapter extends BaseMultiItemQuickAdapter<RSModel, BaseViewHolder> {
    public ItemAdapter() {
      super(new ArrayList<RSModel>());
      addItemType(RS_TYPE_NORMAL, R.layout.item_list);
      addItemType(RS_TYPE_LONG, R.layout.item_list2);
      addItemType(RS_TYPE_IMAGE, R.layout.item_list3);
    }

    @Override protected void convert(BaseViewHolder helper, RSModel item) {
      switch (item.getItemType()) {
        case RS_TYPE_NORMAL:
          helper.setText(R.id.tv_text, item.getContent());
          break;
        case RS_TYPE_LONG:
          helper.setText(R.id.tv_text, item.getContent());
          break;
        case RS_TYPE_IMAGE:
          helper.setImageResource(R.id.iv_img, R.drawable.ic_launcher);
      }
    }
  }
}
