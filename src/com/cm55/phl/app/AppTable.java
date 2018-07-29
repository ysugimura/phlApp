// Created by Cryptomedia Co., Ltd. 2006/07/23
package com.cm55.phl.app;

import com.cm55.phl.*;
import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * このアプリケーションのテーブルに共通する機能
 */
public class AppTable extends Table {

  public static final int DELETEDFIELD = 1; // 削除済み
  public static final int DATEFIELD = 10;   // 日付
  public static final int TIMEFIELD = 8;    // 時刻
  public static final int BARFIELD = 13;    // バーコード

  @TBLFLD(number=0, size=1)
  public DeletedField delField;

  @TBLFLD(number=1, size=10)
  public TblField dateField;

  @TBLFLD(number=2, size=8)
  public TblField timeField;

  @TBLFLD(number=3,size=13)
  public TblField barField;

  public AppTable(String name, boolean init) {
    super(new SJIS(name), 0);
    delField = new DeletedField(DELETEDFIELD);
    dateField = new TblField(DATEFIELD);
    timeField = new TblField(TIMEFIELD);
    barField = new TblField(BARFIELD);
    if (init) {
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
        });
      }
    }
  }

  /** レコード削除済みフィールド */
  public class DeletedField extends TblField {
    public DeletedField(int size) {
      super(size);
      assert(size == 1);
    }

    /** 有効化する要素を作成 */
    public Macro validateElement() {
      return new Macro() { @Override public void expand(Context ctx) {
        Register datReg = ctx.strReg();
        ctx.proc(
            new Assign(datReg, " "),
            setValueElement(datReg)
        );
        ctx.releaseReg(datReg);
      }};
    }

    /** 無効化する要素を作成 */
    public Macro invalidateElement() {
      return new Macro() { @Override public void expand(Context ctx) {
        Register datReg = ctx.strReg();
        ctx.proc(
            new Assign(datReg, "*"),
            setValueElement(datReg)
        );
        ctx.releaseReg(datReg);
      }};
    }

    /** 有効・無効の切り替え要素を作成する */
    public Macro toggleElement() {
      return new Macro() { @Override public void expand(Context ctx) {
        Register datReg = ctx.strReg();
        ctx.proc(
            getValueElement(datReg),
            new If(datReg, " ",
                new Assign(datReg, "*"),
                new Assign(datReg, " ")
            ),
            setValueElement(datReg)
        );
        ctx.releaseReg(datReg);
      }};
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  //
  /////////////////////////////////////////////////////////////////////////////

  /** 数量のみ必要なタイプ */
  public static class CountType extends AppTable {

    public static final int CNTFIELD = 4; // 数量
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + CNTFIELD;

    @TBLFLD(number=0,size=4)
    public TblField cntField;

    public CountType(String filename, boolean init) {
      super(filename, false);
      cntField = new TblField(CNTFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            cntField,
        });
      }
    }
  }
  
  /** 期限のみ必要なタイプ */
  public static class LimitType extends AppTable {
    public static final int LIMITFIELD = 8; // 期限
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + LIMITFIELD;

    @TBLFLD(number=0,size=8)
    public TblField limitField;

    public LimitType(String filename, boolean init) {
      super(filename, false);
      limitField = new TblField(LIMITFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            limitField,
        });
      }
    }
  }
  
  /** 金額のみ必要なタイプ */
  public static class PriceType extends AppTable {

    public static final int PRICEFIELD = 7; // 金額
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + PRICEFIELD;

    @TBLFLD(number=0,size=PRICEFIELD)
    public TblField priceField;

    public PriceType(String filename, boolean init) {
      super(filename, false);
      priceField = new TblField(PRICEFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            priceField,
        });
      }
    }
  }

  /** デポ・数量が必要なタイプ */
  public static class DepotCntType extends AppTable.CountType {
    public static final int DEPOTFIELD = 1; // デポ
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + CNTFIELD + DEPOTFIELD;

    /** ロケーション */
    @TBLFLD(number=0,size=1)
    public TblField depotField;

    public DepotCntType(String filename, boolean init) {
      super(filename, false);
      depotField = new TblField(DEPOTFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            cntField,
            depotField,
        });
      }
    }    
  }
  

  /** デポ・期限が必要なタイプ */
  public static class DepotLimitType extends AppTable.LimitType {
    public static final int DEPOTFIELD = 1; // デポ
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + LIMITFIELD + DEPOTFIELD;

    /** ロケーション */
    @TBLFLD(number=0,size=1)
    public TblField depotField;

    public DepotLimitType(String filename, boolean init) {
      super(filename, false);
      depotField = new TblField(DEPOTFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            limitField,
            depotField,
        });
      }
    } 
  }

  /** ロケ・数量が必要なタイプ */
  public static class LocCntType extends AppTable.CountType {

    public static final int LOCFIELD = 4; // ロケーション
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + CNTFIELD + LOCFIELD;

    /** ロケーション */
    @TBLFLD(number=0,size=4)
    public TblField locField;

    public LocCntType(String filename, boolean init) {
      super(filename, false);
      locField = new TblField(LOCFIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            cntField,
            locField,
        });
      }
    }
  }

  /** 移動元・移動先デポ、数量が必要なタイプ */
  public static abstract class SrcDstType extends AppTable.CountType {
    public static final int SRCFIELD = 1; // 移動元
    public static final int DSTFIELD = 1; // 移動先

    public static final int RECORDLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + CNTFIELD + SRCFIELD + DSTFIELD;

    @TBLFLD(number=0,size=1)
    public TblField srcField;

    @TBLFLD(number=1,size=1)
    public TblField dstField;

    public SrcDstType(String filename, boolean init) {
      super(filename, false);
      cntField = new TblField(CNTFIELD);
      srcField = new TblField(SRCFIELD);
      dstField = new TblField(DSTFIELD);

      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            cntField,
            srcField,
            dstField,
        });
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // 自由入力
  /////////////////////////////////////////////////////////////////////////////
  
  /** 自由１ */
  public static class Free1Type extends AppTable {

    public static final int FREE1FIELD = 6; // 自由１
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + FREE1FIELD;

    @TBLFLD(number=0,size=6)
    public TblField free1Field;

    public Free1Type(String filename, boolean init) {
      super(filename, false);
      free1Field = new TblField(FREE1FIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            free1Field,
        });
      }
    }
  }
  
  /** 自由2 */
  public static class Free2Type extends Free1Type {

    public static final int FREE2FIELD = 6; // 自由2
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + FREE1FIELD + FREE2FIELD;

    @TBLFLD(number=0,size=6)
    public TblField free2Field;

    public Free2Type(String filename, boolean init) {
      super(filename, false);
      free2Field = new TblField(FREE2FIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            free1Field,
            free2Field,
        });
      }
    }
  }
  
  /** 自由3 */
  public static class Free3Type extends Free2Type {

    public static final int FREE3FIELD = 6; // 自由3
    public static final int RECLEN =
      DELETEDFIELD + DATEFIELD + TIMEFIELD + BARFIELD + FREE1FIELD + FREE2FIELD + FREE3FIELD;

    @TBLFLD(number=0,size=6)
    public TblField free3Field;

    public Free3Type(String filename, boolean init) {
      super(filename, false);
      free3Field = new TblField(FREE3FIELD);
      if (init) {
        setFields(new TblField[] {
            delField,
            dateField,
            timeField,
            barField,
            free1Field,
            free2Field,
            free3Field
        });
      }
    }
  }
}
