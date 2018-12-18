package com.exp.androidtv.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by haiyu.chen on 2018/12/17.
 */

public class RSModel implements MultiItemEntity {

  public static final int RS_TYPE_NORMAL = 0;
  public static final int RS_TYPE_LONG = 1;
  public static final int RS_TYPE_IMAGE = 2;

  private String content;
  private int itemType;

  public RSModel(String content, int itemType) {
    this.content = content;
    this.itemType = itemType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setItemType(int itemType) {
    this.itemType = itemType;
  }

  @Override public int getItemType() {
    return itemType;
  }
}
