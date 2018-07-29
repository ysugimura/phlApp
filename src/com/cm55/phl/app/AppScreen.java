// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import static com.cm55.phl.app.Consts.*;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * スクリーン
 */
class AppScreen<T extends AppTable> extends Screen {

  /** バーコードフィールド */
  public ScrField barField;

  /** 削除フィールド */
  public DelScrField delField;

  protected AppScreen(T table, boolean init) {
    this(table, init, LINE1);
  }
  protected AppScreen(T table, boolean init, int barLine) {
    barField =
      new BarScrField(barLine, 0, "J[             ]", 2, table.barField);
    delField = new DelScrField(LINE3, 0, table.delField);
    setDefaultFullAction(FullAction.NOTHING);
    if (init) {
        setFields(new ScrField[] {
            barField,
            delField
        });
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  //
  /////////////////////////////////////////////////////////////////////////////

  /** 数量のみ必要なもの */
  public static class CountType<T extends AppTable.CountType> extends AppScreen<T> {
    public ScrField cntField;
    public CountType(T table, boolean init) {
      super(table, false);
      cntField = new IntScrField(LINE3, 8, "数[    ]", 3, table.cntField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            cntField,
            delField
        });
      }
    }
  }

  /** 期限のみ必要なもの */
  public static class LimitType<T extends AppTable.LimitType> extends AppScreen<T> {
    public ScrField limitField;
    public LimitType(T table, boolean init) {
      super(table, false);
      limitField = new IntScrField(LINE3, 2, "期限[        ]", 5, table.limitField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            limitField,
            delField
        });
      }
    }
  }
  
  /** 金額のみ必要なもの */
  public static class PriceType<T extends AppTable.PriceType> extends AppScreen<T> {
    public ScrField priceField;
    public PriceType(T table, boolean init) {
      super(table, false);
      priceField = new IntScrField(LINE3, 7, "[       ]", 1, table.priceField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            priceField,
            delField
        });
      }
    }
  }
  
  /** デポと数量が必要なもの */
  public static class DepotCntType<T extends AppTable.DepotCntType> extends AppScreen.CountType<T> {

    /** デポ */
    public ScrField depotField;

    public DepotCntType(T table, boolean init) {
      super(table, false);
      depotField =
        new IntScrField(LINE0, 0, "デポ[ ]", 5, table.depotField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            delField,
            depotField,
            cntField,
        });
      }
    }
  }
  
  /** デポと期限が必要なもの */
  public static class DepotLimitType<T extends AppTable.DepotLimitType> extends AppScreen.LimitType<T> {

    /** デポ */
    public ScrField depotField;

    public DepotLimitType(T table, boolean init) {
      super(table, false);
      depotField =
        new IntScrField(LINE0, 0, "デポ[ ]", 5, table.depotField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            delField,
            depotField,
            limitField,
        });
      }
    }
  }
  
  /** ロケと数量が必要なもの */
  public static class LocCntType<T extends AppTable.LocCntType> extends AppScreen.CountType<T> {

    /** ロケーション */
    public ScrField locField;

    public LocCntType(T table, boolean init) {
      super(table, false);
      locField =
        new IntScrField(LINE0, 0, "ロケ[    ]", 5, table.locField);
      if (init) {
        setFields(new ScrField[] {
            barField,
            delField,
            locField,
            cntField,
        });
      }
    }
  }

  /** 移動など移動元・移動先と数量が必要なもの */
  public static class SrcDstType<T extends AppTable.SrcDstType> extends AppScreen.CountType<T> {

    /** 移動元 */
    public ScrField srcField;

    /** 移動先 */
    public ScrField dstField;

    public SrcDstType(T table, boolean init) {
      super(table, false);
      srcField =
        new IntScrField(LINE0, 0, "移[ ]→", 3, table.srcField);
      dstField =
        new IntScrField(LINE0, 7, "[ ]", 1, table.dstField);
      if (init) {
        setFields(new ScrField[] {
          srcField,
          dstField,
          barField,
          delField,
          cntField,
        });
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // 自由入力
  /////////////////////////////////////////////////////////////////////////////
  
  /** 自由１ */
  public static class Free1Type<T extends AppTable.Free1Type> extends AppScreen<T> {
    public ScrField free1Field;
    public Free1Type(T table, boolean init) {
      super(table, false, LINE0);
      free1Field = new IntScrField(LINE2, 0, "[      ]", 1, table.free1Field);
      if (init) {
        setFields(new ScrField[] {
            barField,
            free1Field,
            delField
        });
      }
    }
  }

  /** 自由２ */
  public static class Free2Type<T extends AppTable.Free2Type> extends Free1Type<T> {
    public ScrField free2Field;
    public Free2Type(T table, boolean init) {
      super(table, false);
      free2Field = new IntScrField(LINE2, 8, "[      ]", 1, table.free2Field);
      if (init) {
        setFields(new ScrField[] {
            barField,
            free1Field,
            free2Field,
            delField
        });
      }
    }
  }
  
  /** 自由3 */
  public static class Free3Type<T extends AppTable.Free3Type> extends Free2Type<T> {
    public ScrField free3Field;
    public Free3Type(T table, boolean init) {
      super(table, false);
      free3Field = new IntScrField(LINE3, 8, "[      ]", 1, table.free3Field);
      if (init) {
        setFields(new ScrField[] {
            barField,
            free1Field,
            free2Field,
            free3Field,
            delField
        });
      }
    }
  }
  
}
