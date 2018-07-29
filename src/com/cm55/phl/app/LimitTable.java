package com.cm55.phl.app;

/**
 * 期限テーブル
 * デポと数量
 */
public class LimitTable extends AppTable.DepotLimitType {
  public static final String FILENAME = "limit.dat";
  public LimitTable() {
    super(FILENAME, true);
  }
}
