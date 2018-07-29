// Created by Cryptomedia Co., Ltd. 2006/06/11
package com.cm55.phl.app;



/** 移動処理データテーブル */
public class MoveTable extends AppTable.SrcDstType {
  public static final String FILENAME = "move.dat";
  public MoveTable() {
    super(FILENAME, true);
  }
}

