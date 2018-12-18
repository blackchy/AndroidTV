package com.exp.androidtv.ui.widget;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exp.androidtv.R;

/**
 * Created by haiyu.chen on 2018/12/14.
 */

public class ListItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
  public ListItemAdapter() {
    super(R.layout.item_list);
  }

  @Override protected void convert(BaseViewHolder helper, String item) {
    helper.setText(R.id.tv_text, item);
  }
}
