// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

/**
 * 返品テーブル
 * デポと数量
 */
public class ReturnTable extends AppTable.DepotCntType {
  public static final String FILENAME = "return.dat";
  public ReturnTable() {
    super(FILENAME, true);
  }
}
