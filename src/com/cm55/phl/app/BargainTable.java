// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;


/**
 * 特売データテーブル
 *
 */
public class BargainTable extends AppTable.PriceType {

  public static final String FILENAME = "bargain.dat";

  public static final int RECLEN =
    DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + PRICEFIELD;

  public BargainTable() {
    super(FILENAME, true);
  }
}
