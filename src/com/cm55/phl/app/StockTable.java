// Created by Cryptomedia Co., Ltd. 2006/06/30
package com.cm55.phl.app;


/**
 * 在庫数確認用テーブル
 */
public class StockTable extends AppTable.LocCntType {

  public static final String FILENAME = "stock.dat";
  public static final int RECLEN =
    DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + CNTFIELD + LOCFIELD;

  public StockTable() {
    super(FILENAME, true);
  }
}
