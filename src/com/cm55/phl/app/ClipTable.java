package com.cm55.phl.app;

/**
 * クリップデータテーブル
 *
 */
public class ClipTable extends AppTable {

  public static final String FILENAME = "clip.dat";

  public static final int RECLEN =
    DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD;

  public ClipTable() {
    super(FILENAME, true);
  }
}
