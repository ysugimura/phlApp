// Created by Cryptomedia Co., Ltd. 2006/08/20
package com.cm55.phl.app;


/**
 * 入荷用テーブル
 */
public class IncomingTable extends AppTable.CountType {
  public static final String FILENAME = "incoming.dat";
  public IncomingTable() {
    super(FILENAME, true);
  }
}
