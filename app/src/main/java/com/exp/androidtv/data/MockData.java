package com.exp.androidtv.data;

import com.exp.androidtv.model.GNList;
import com.exp.androidtv.model.RNList;
import com.exp.androidtv.model.RSModel;
import java.util.Arrays;
import java.util.List;

import static com.exp.androidtv.model.RSModel.RS_TYPE_IMAGE;
import static com.exp.androidtv.model.RSModel.RS_TYPE_LONG;
import static com.exp.androidtv.model.RSModel.RS_TYPE_NORMAL;

/**
 * Created by haiyu.chen on 2018/12/14.
 */

public class MockData {
  private static volatile MockData instance = null;

  private MockData() {
  }

  public static MockData getInstance() {
    if (instance == null) {
      synchronized (MockData.class) {
        if (instance == null) {
          instance = new MockData();
        }
      }
    }
    return instance;
  }

  public List<String> mainItems =
      Arrays.asList("Google横向列表", "Google纵向列表", "Google嵌套列表", "RecyclerView横向列表",
          "RecyclerView纵向列表", "RecyclerView嵌套列表", "RecyclerView网格列表", "RecyclerView不同item", "暗焦点");

  public List<String> listItems =
      Arrays.asList("项0", "项1", "项2", "项3", "项4", "项5", "项6", "项7", "项8", "项9", "项10", "项11",
          "项12");

  public List<GNList> gnLists =
      Arrays.asList(new GNList(listItems), new GNList(listItems), new GNList(listItems),
          new GNList(listItems), new GNList(listItems));

  public List<RNList> rnLists =
      Arrays.asList(new RNList(listItems), new RNList(listItems), new RNList(listItems),
          new RNList(listItems), new RNList(listItems));

  public List<RSModel> rsModels =
      Arrays.asList(new RSModel("项0", RS_TYPE_NORMAL), new RSModel("项1", RS_TYPE_NORMAL),
          new RSModel("项2", RS_TYPE_NORMAL), new RSModel("项3", RS_TYPE_NORMAL),
          new RSModel("项4", RS_TYPE_LONG), new RSModel("项5", RS_TYPE_LONG),
          new RSModel("项6", RS_TYPE_IMAGE), new RSModel("项7", RS_TYPE_NORMAL),
          new RSModel("项8", RS_TYPE_NORMAL), new RSModel("项9", RS_TYPE_NORMAL),
          new RSModel("项10", RS_TYPE_IMAGE), new RSModel("项11", RS_TYPE_NORMAL));

  public List<String> outMenu = Arrays.asList("菜单0", "菜单1", "菜单2", "菜单3", "菜单4", "菜单5");
}
