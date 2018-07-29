// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;


import static com.cm55.phl.app.Consts.*;

import java.util.*;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/** 入力処理 */
@SuppressWarnings("rawtypes")
abstract class InputProc<T extends AppTable, S extends AppScreen> extends Macro {

  /** テーブル */
  protected T appTable;

  /** 編集画面 */
  protected S appScreen;

  /** マスタテーブル */
  protected MasterTable masterTable = new MasterTable();

  /** 商品表示行 */
  protected int goodsLine = LINE2;
  
  /** 作成 */
  protected void init(T appTable, S appScreen) {
    this.appTable = appTable;
    this.appScreen = appScreen;
  }

  protected void setGoodsLine(int line) {
    goodsLine = line;    
  }
  
  Label appTopLabel;
  Label procLoopLabel;

  /** 動作モードを表すレジスタ */
  protected Register modeReg;

  /** 再描画禁止レジスタ */
  protected Register noRedrawReg;

  protected static final int MODE_NEW = 0;
  protected static final int MODE_EDIT = 1;
  protected static final int MODE_INFO = 2;

  // 入力時の特殊キー
  EnumSet<Key>specialKeys = Key.getKeySet(
      new Key[] { INFO_KEY, QUIT_KEY, PREV_KEY, NEXT_KEY, TOP_KEY, NEW_KEY }
  );

  // 特殊キー処理ラベル
  Label specialLabel = new Label();

  /** 取得 */
  @Override
  protected void expand(Context ctx) {
    assert(appTable != null);

    appTable.allocReg(ctx);
    masterTable.allocReg(ctx);
    modeReg = ctx.intReg();
    noRedrawReg = ctx.intReg();
    ctx.proc(
        // トップにラベル
        appTopLabel = new Label(),

        new InfiniteLoop(
            new Openning(),
            new Assign(modeReg, MODE_NEW),
            new Assign(noRedrawReg, 0),

            // 処理ループにラベル
            procLoopLabel = new Label(),
            new InfiniteLoop(
                // モードによって追加か編集
                new If(modeReg, 0,
                    getNewProc(),
                    new EditProc()
                ),

                // 正常終了時は次へ
                new Continue(procLoopLabel),

                // 特殊キーラベルを置く
                specialLabel,
                new SpecialProc()
            )
        )
    );
    appTable.releaseReg(ctx);
    masterTable.releaseReg(ctx);
    ctx.releaseReg(modeReg);
    ctx.releaseReg(noRedrawReg);
  }

  /** オープニング */
  private class Openning extends Macro {
    protected void expand(Context ctx) {

      final Register recordCnt = ctx.intReg();
      ctx.proc(
          // 画面クリア。レコード数取得
          new DisplayClear(),
          appTable.recordCountElement(recordCnt),

          // レコード数により表示切り替え
          new If(recordCnt, Comp.GT, 0,
              /*
              new Compound(
                  new DisplayString(LINE1, 0, "入力済のデータは"),
                  new DisplayString(LINE2, 0, "ありません")
              ),
              */
              new Macro() { protected void expand(Context ctx) {
                Register statusReg = ctx.intReg();
                Register numStrReg = ctx.strReg();
                Register inputKey = ctx.intReg();
                ctx.proc(
                  new DisplayString(LINE0, 0, "入力済件数"),
                  new Assign(numStrReg, recordCnt),
                  new StringShift(numStrReg, 6, true),
                  new DisplayRegister(LINE0, 10, numStrReg, 0, 6),
                  appTable.bottomElement(statusReg),
                  new DisplayString(LINE1, 6, "最終入力"),
                  appTable.dateField.displayElement(LINE2, 6),
                  appTable.timeField.displayElement(LINE3, 6),
                  new NoEchoInput(inputKey),
                  new IfBreak(inputKey, QUIT_KEY, appTopLabel)

                );
                ctx.releaseReg(statusReg, numStrReg, inputKey);
              }}

          )
      );
      ctx.releaseReg(recordCnt);

      /*
      Register inputKey = ctx.intReg();
      ctx.proc(
          // キー入力。QUIT_KEYならトップ処理ループを抜ける
          new NoEchoInput(inputKey),
          new IfBreak(inputKey, QUIT_KEY, appTopLabel)
      );
      ctx.releaseReg(inputKey);
      */
    }
  }

  /** 新規入力処理を取得する */
  protected Macro getNewProc() {
    return new NewProc();
  }

  /** 新規入力 */
  private class NewProc extends Macro {
    protected void expand(Context ctx) {
      Register pauseReg = ctx.intReg();
      ctx.proc(
          new InfiniteLoop(
              new DisplayClear(),
              appTable.delField.setValueElement(" "),
              new DisplayPartClear(goodsLine, 0, 16),
              //appScreen.cntField.clearElement(),
              appScreen.barField.inputElement(specialKeys, specialLabel),
              new MasterSearch(),
              //appScreen.cntField.inputElement(specialKeys, specialLabel),
              new NoEchoInput(pauseReg),
              appTable.dateField.setValueElement(Register.DATE1),
              appTable.timeField.setValueElement(Register.TIME1),
              appTable.appendElement()
          )
      );
      ctx.releaseReg(pauseReg);
    }
  }

  /** レコード編集 */
  private class EditProc extends Macro {
    protected void expand(Context ctx) {

      Register inputReg = ctx.intReg();
      ctx.proc(
          new If(noRedrawReg, 0,
              new Compound(
                  new DisplayClear(),
                  appScreen.displayAllElement(),

                  // マスタ検索は行わない
                  new DisplayString(goodsLine, 0, "[編集中・非表示]").setClear(16)
              ),
              new Assign(noRedrawReg, 0)
          ),
          new InfiniteLoop(
              new EchoedInput(LINE3, 0, inputReg, 1, specialKeys)
                .setFullAction(FullAction.IMMEDIATE)
                .setNoEcho(true),
              new If(Register.RSLT, 0,
                  new Compound(
                      new If(inputReg, 0,
                          appTable.delField.invalidateElement(),
                          appTable.delField.validateElement()
                      ),
                      appScreen.delField.displayElement(),
                      appTable.overwriteElement(),
                      new Continue()
                  ),
                  new Jump(specialLabel)
              )
          )
      );
      ctx.releaseReg(inputReg);
    }
  }

  /**
   * 特殊処理。
   * 行入力、バーコード入力時に指定された特殊キーが押されたとき、
   * RSLTレジスタにそのキーコードが格納された状態で呼ばれる。
   */
  private class SpecialProc extends Macro {
    protected void expand(Context ctx) {
      Register statusReg = ctx.intReg();
      ctx.proc(
          new Case(Register.RSLT, new Object[] {

              // 上位に抜ける
              QUIT_KEY,  new Break(appTopLabel),

              // 前のレコードへ
              PREV_KEY, new If(modeReg,  MODE_NEW,
                  new Compound( // 新規だったら、ボトム
                      appTable.bottomElement(statusReg),
                      new If (statusReg, 0, new Assign(modeReg, MODE_EDIT))
                  ), // ELSE
                  new Compound( // 編集中であれば前のレコード
                      appTable.previousElement(statusReg),
                      new If(statusReg, Comp.NE, 0, new Assign(noRedrawReg, 1))
                  )
              ),

              // 後のレコードへ、編集中のみ可能
              NEXT_KEY, new If(modeReg, MODE_EDIT, new Compound(
                  appTable.nextElement(statusReg),
                  new If(statusReg, Comp.NE, 0, new Assign(noRedrawReg, 1))
              )),

              // 最初のレコードへ
              TOP_KEY, new Compound(
                  appTable.topElement(statusReg), // とにかく最初へ
                  // 成功ならモード設定
                  new If(statusReg, 0, new Assign(modeReg, MODE_EDIT))
              ),

              // 新規作成。モードを「新規」に
              NEW_KEY, new Assign(modeReg, MODE_NEW),

              // 情報表示
              INFO_KEY, new Compound(
                  new If(modeReg, MODE_EDIT, new InfoProc())
              )
          })
      );
      ctx.releaseReg(statusReg);
    }
  }

  /** マスタ検索 */
  protected class MasterSearch extends Macro {
    protected void expand(Context ctx) {
      Register searchKey = ctx.strReg();
      ctx.proc(
          // バーコードフィールド値を取得し、マスタ検索
          appTable.barField.getValueElement(searchKey),
          masterTable.searchElement(masterTable.barcode, searchKey),
          new If(Register.RSLT, 0,
              masterTable.name.displayElement(goodsLine, 0),
              // else
              new DisplayString(goodsLine, 0, "[商品不明]").setClear(16)
          )
      );
      ctx.releaseReg(searchKey);
    }
  }

  /** 情報表示用画面 */
  private class InfoScreen extends Screen {
    public ScrField dateField;
    public ScrField timeField;

    private InfoScreen(T table) {
      setFields(new ScrField[] {
          dateField =
            new StrScrField(LINE1, 0, "日付[          ]", 5, table.dateField),
          timeField =
            new StrScrField(LINE2, 0, "時刻[        ]", 5, table.timeField),
      });
    }
  }

  /** 情報表示 */
  private class InfoProc extends Macro {
    InfoScreen infoScreen = new InfoScreen(appTable);
    protected void expand(Context ctx) {
      Register keyIn = ctx.intReg();
      ctx.proc(
          new DisplayClear(),
          new DisplayString(LINE0, 0, "　＊入力情報＊　"),
          infoScreen.dateField.displayElement(),
          infoScreen.timeField.displayElement(),
          new NoEchoInput(keyIn),
          new IfBreak(keyIn, QUIT_KEY, appTopLabel)
      );
      ctx.releaseReg(keyIn);
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  // 数量のみ必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class CountType<T extends AppTable.CountType, S extends AppScreen.CountType<?>> extends InputProc<T,S> {
    protected Macro getNewProc() { return new NewProc(); }


    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.cntField.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.cntField.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // 期限のみ必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class LimitType<T extends AppTable.LimitType, S extends AppScreen.LimitType<?>> extends InputProc<T,S> {
    protected Macro getNewProc() { return new NewProc(); }


    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.limitField.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.limitField.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  // 金額のみ必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class PriceType<T extends AppTable.PriceType, S extends AppScreen.PriceType<?>> extends InputProc<T,S> {
    protected Macro getNewProc() { return new NewProc(); }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.priceField.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.priceField.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  // デポと数量が必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class DepotCntType<T extends AppTable.DepotCntType, S extends AppScreen.DepotCntType<?>> extends InputProc<T,S> {

    /** 新規処理マクロ */
    protected Macro getNewProc() {
      return new NewProc();
    }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        Label stockLoopLabel;
        ctx.proc(
            stockLoopLabel = new Label(),
            new InfiniteLoop(
                new DisplayClear(),
                appScreen.depotField.inputElement(specialKeys, specialLabel),
                new InfiniteLoop(
                    appTable.delField.setValueElement(" "),
                    new DisplayPartClear(goodsLine, 0, 16),
                    appScreen.cntField.clearElement(),
                    appScreen.barField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    new MasterSearch(),
                    appScreen.cntField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    appTable.dateField.setValueElement(Register.DATE1),
                    appTable.timeField.setValueElement(Register.TIME1),
                    appTable.appendElement()
                )
            )
        );
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // デポと期限が必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class DepotLimitType<T extends AppTable.DepotLimitType, S extends AppScreen.DepotLimitType<?>> extends InputProc<T,S> {

    /** 新規処理マクロ */
    protected Macro getNewProc() {
      return new NewProc();
    }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        Label stockLoopLabel;
        ctx.proc(
            stockLoopLabel = new Label(),
            new InfiniteLoop(
                new DisplayClear(),
                appScreen.depotField.inputElement(specialKeys, specialLabel),
                new InfiniteLoop(
                    appTable.delField.setValueElement(" "),
                    new DisplayPartClear(goodsLine, 0, 16),
                    appScreen.limitField.clearElement(),
                    appScreen.barField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    new MasterSearch(),
                    appScreen.limitField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    appTable.dateField.setValueElement(Register.DATE1),
                    appTable.timeField.setValueElement(Register.TIME1),
                    appTable.appendElement()
                )
            )
        );
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // ロケーションと数量が必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static abstract class LocCntType<T extends AppTable.LocCntType, S extends AppScreen.LocCntType<?>> extends InputProc<T,S> {

    /** 新規処理マクロ */
    protected Macro getNewProc() {
      return new NewProc();
    }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        Label stockLoopLabel;
        ctx.proc(
            stockLoopLabel = new Label(),
            new InfiniteLoop(
                new DisplayClear(),
                appScreen.locField.inputElement(specialKeys, specialLabel),
                new InfiniteLoop(
                    appTable.delField.setValueElement(" "),
                    new DisplayPartClear(goodsLine, 0, 16),
                    appScreen.cntField.clearElement(),
                    appScreen.barField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    new MasterSearch(),
                    appScreen.cntField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(stockLoopLabel) },
                        new Jump(specialLabel)
                    ),
                    appTable.dateField.setValueElement(Register.DATE1),
                    appTable.timeField.setValueElement(Register.TIME1),
                    appTable.appendElement()
                )
            )
        );
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  // 移動入力など、移動元・移動先と数量が必要なもの
  /////////////////////////////////////////////////////////////////////////////

  public static class SrcDstType<T extends AppTable.SrcDstType, S extends AppScreen.SrcDstType<?>> extends InputProc<T,S> {

    protected Macro getNewProc() {
      return new NewProc();
    }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        Label loopLabel;
        ctx.proc(
            loopLabel = new Label(),
            new InfiniteLoop(
                new DisplayClear(),
                appScreen.srcField.inputElement(specialKeys, specialLabel),
                appScreen.dstField.inputElement(specialKeys, specialLabel),
                new InfiniteLoop(
                    new DisplayPartClear(goodsLine, 0, 16),
                    appTable.delField.setValueElement(" "),
                    appScreen.cntField.clearElement(),
                    appScreen.barField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(loopLabel) },
                        new Jump(specialLabel)
                    ),
                    new MasterSearch(),
                    appScreen.cntField.inputElement(specialKeys,
                        new Object[] { Key.Q2, new Continue(loopLabel) },
                        new Jump(specialLabel)
                    ),
                    appTable.dateField.setValueElement(Register.DATE1),
                    appTable.timeField.setValueElement(Register.TIME1),
                    appTable.appendElement()
                )
            )
        );
      }
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // 自由入力
  /////////////////////////////////////////////////////////////////////////////
  
  public static abstract class Free1Type<T extends AppTable.Free1Type, S extends AppScreen.Free1Type<?>> extends InputProc<T,S> {
    
    protected Free1Type() {
      setGoodsLine(LINE1);
    }
    
    protected Macro getNewProc() { return new NewProc(); }

    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.free1Field.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.free1Field.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }
  
  public static abstract class Free2Type<T extends AppTable.Free2Type, S extends AppScreen.Free2Type<?>> extends InputProc<T,S> {
    protected Macro getNewProc() { return new NewProc(); }

    protected Free2Type() {
      setGoodsLine(LINE1);
    }
    
    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.free1Field.clearElement(),
                appScreen.free2Field.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.free1Field.inputElement(specialKeys, specialLabel),
                appScreen.free2Field.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }
  
  public static abstract class Free3Type<T extends AppTable.Free3Type, S extends AppScreen.Free3Type<?>> extends InputProc<T,S> {
    protected Macro getNewProc() { return new NewProc(); }

    protected Free3Type() {
      setGoodsLine(LINE1);
    }
    
    /** 新規入力 */
    private class NewProc extends Macro {
      protected void expand(Context ctx) {
        ctx.proc(
            new InfiniteLoop(
                new DisplayClear(),
                appTable.delField.setValueElement(" "),
                new DisplayPartClear(goodsLine, 0, 16),
                appScreen.free1Field.clearElement(),
                appScreen.free2Field.clearElement(),
                appScreen.free3Field.clearElement(),
                appScreen.barField.inputElement(specialKeys, specialLabel),
                new MasterSearch(),
                appScreen.free1Field.inputElement(specialKeys, specialLabel),
                appScreen.free2Field.inputElement(specialKeys, specialLabel),
                appScreen.free3Field.inputElement(specialKeys, specialLabel),
                appTable.dateField.setValueElement(Register.DATE1),
                appTable.timeField.setValueElement(Register.TIME1),
                appTable.appendElement()
            )
        );
      }
    }
  }
}
