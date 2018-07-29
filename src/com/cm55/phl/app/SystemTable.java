// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.*;
import com.cm55.phl.gen.*;

/** システムテーブル */
public class SystemTable extends Table {

  public static final String FILENAME = "system.dat";
  public static final int PROCNOFIELD = 2; // 処理番号フィールド
  public static final int FILLERFIELD = 198; // 余白
  public static final int RECORDLEN = 200;

  public TblField procNoField;
  public TblField fillerField;

  public SystemTable() {
    super(new SJIS(FILENAME), RECORDLEN);
    setFields(new TblField[] {
        procNoField = new TblField(PROCNOFIELD),
        fillerField = new TblField(FILLERFIELD),
    });
  }
}
