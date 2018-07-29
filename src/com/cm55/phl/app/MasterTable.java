// Created by Cryptomedia Co., Ltd. 2006/06/11
package com.cm55.phl.app;

import com.cm55.phl.*;
import com.cm55.phl.gen.*;


/*
 * 13桁：バーコード（１３桁に満たない場合は左づめ、右側空白）
 * 10桁：商品名文字列
 * 7桁:価格文字列
 * 2桁：不明（おそらくCRLF）
 */
class MasterTable extends Table {

  public static final SJIS NAME = new SJIS("master.dat");

  public TblField barcode;
  public TblField name;
  public TblField price;
  public TblField crlf;

  public MasterTable() {
    super(NAME, 0);
    setFields(new TblField[] {
        barcode = new TblField(13),
        name = new TblField(10),
        price = new TblField(7),
        crlf = new TblField(2),
    });
  }
}
