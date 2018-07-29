// Created by Cryptomedia Co., Ltd. 2006/06/08
package com.cm55.phl.app;

import static com.cm55.phl.app.Consts.*;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * PHL用アプリケーション
 */
public class PHLApp extends Macro {

  public static final String VERSION = "1.40";
  static Profile profile;

  /** ジェネレータ取得 */
  public static Generator get(Profile aProfile) {

    profile = aProfile;
    Title title = new Title("商品ｽﾀｯﾌ for PHL", VERSION);
    title.setStartScreen(StartScreen.TWOSECONDS);
    title.setProfile(profile);
    title.setStartScreen(StartScreen.NONE);
    return new Generator(title, new Compound(
        new DisplayClear(),
        new PHLApp()
    ));
  }

  /** トップメニュー */
  protected void expand(Context ctx) {
    ctx.proc(
        new TimeProc(),
        new MainProc()
    );
  }

  /**
   * 日時とハンディ番号を確認する。
   * ENTキーが押されたら終了する。それ以外のキーで再表示
   */
  public static class TimeProc extends Macro {

    @Override
    protected void expand(Context ctx) {
      Register inputReg = ctx.allocReg(Type.INTEGER);
      ctx.proc(new InfiniteLoop(
          //new DisplayString(LINE0, 0, " 日時・HTID確認 "),
          new DisplayString(LINE0, 0, "PHLApp " + VERSION),
          new DisplayRegister(LINE1, 0, Register.DATE1, 0, 10),
          new DisplayRegister(LINE2, 0, Register.TIME1, 0, 8),
          new DisplayString(LINE2, 10, "ID:"),
          new DisplayRegister(LINE2, 13, Register.HTID, 0, 3),
          new DisplayString(LINE3, 0, "ENT以外で再表示"),
          new NoEchoInput(inputReg),
          new IfBreak(inputReg, Key.ENT)
      ));
      ctx.releaseReg(inputReg);
    }
  }

  /**
   * メイン処理
   */
  public static class MainProc extends Macro {

    private static final int MOVE_PROC = 1;
    private static final int STOCK_PROC = 2;
    private static final int INCOMING_PROC = 3;
    private static final int BARGAIN_PROC = 4;
    private static final int LOSS_PROC = 5;
    private static final int RETURN_PROC = 6;
    private static final int LIMIT_PROC = 7;
    private static final int CLIP_PROC = 8;
    private static final int PRICE_PROC = 9;
    private static final int FREE1_PROC = 10;
    private static final int FREE2_PROC = 11;
    private static final int FREE3_PROC = 12;
    private static final int MASTER_PROC = 13;
    
    SystemTable systemTable = new SystemTable();

    @Override
    protected void expand(Context ctx) {
      systemTable.allocReg(ctx);
      Register statusReg = ctx.intReg();
      Register procNoReg = ctx.intReg();

      Label infiniteLabel = new Label();
      ctx.proc(
          // システムテーブルを読み出し、処理番号を取得
          systemTable.topElement(statusReg),
          new If(statusReg, Comp.EQ, 0,
              new Compound(
                  systemTable.procNoField.getValueElement(procNoReg)
              ),
              new Assign(procNoReg, 0)
          ),

          // 処理
          new InfiniteLoop(
              new If(procNoReg, 0, new Compound(
                  infiniteLabel, new InfiniteLoop(
                      new MenuLoop(
                          new String[] {
                              "Q1次 メイン",
                              "1.移動処理", // 1
                              "2.棚卸処理", // 2
                              "3.入荷処理", // 3
                              "Q1次 メイン Q2前",
                              "1.特売選択", // 4
                              "2.ロス処理", // 5
                              "3.返品処理", // 6
                              "Q1次 メイン Q2前",
                              "1.期限処理", // 7
                              "2.クリップ", // 8                              
                              "3.売価表示", // 9
                              "　　 メイン Q2前",
                              "1.自由入力1", // 10
                              "2.自由入力2", // 11
                              "3.自由入力3", // 12
                              "　　 メイン Q2前",
                              "1.マスタ受信", // 13
                          },
                          new Object[] {
                              null, // ガイド
                              new Compound(new Assign(procNoReg, MOVE_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, STOCK_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, INCOMING_PROC), new Break(infiniteLabel)),
                              null, // ガイド
                              new Compound(new Assign(procNoReg, BARGAIN_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, LOSS_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, RETURN_PROC), new Break(infiniteLabel)),
                              null, // ガイド
                              new Compound(new Assign(procNoReg, LIMIT_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, CLIP_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, PRICE_PROC), new Break(infiniteLabel)),
                              null, // ガイド
                              new Compound(new Assign(procNoReg, FREE1_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, FREE2_PROC), new Break(infiniteLabel)),
                              new Compound(new Assign(procNoReg, FREE3_PROC), new Break(infiniteLabel)),
                              null, // ガイド
                              new Compound(new Assign(procNoReg, MASTER_PROC), new Break(infiniteLabel)),
                          },
                          Key.Q1,
                          Key.Q2
                      )
                  )
              )),

              // 選択があればそれをシステムテーブルに書込み
              new If(procNoReg, Comp.NE, 0, new Compound(
                  systemTable.procNoField.setValueElement(procNoReg),
                  systemTable.overwriteElement()
              )),

              // 各処理を起動
              new Case(procNoReg, new Object[] {
                  MOVE_PROC,     new ProcMenuProc(new MoveProc()),
                  STOCK_PROC,    new ProcMenuProc(new StockProc()),
                  INCOMING_PROC, new ProcMenuProc(new IncomingProc()),
                  BARGAIN_PROC,  new ProcMenuProc(new BargainProc()),
                  LOSS_PROC,     new ProcMenuProc(new LossProc()),
                  RETURN_PROC,   new ProcMenuProc(new ReturnProc()),
                  LIMIT_PROC,    new ProcMenuProc(new LimitProc()),
                  CLIP_PROC,     new ProcMenuProc(new ClipProc()),
                  PRICE_PROC,    new PriceProc(),
                  FREE1_PROC,    new ProcMenuProc(new Free1Proc()),
                  FREE2_PROC,    new ProcMenuProc(new Free2Proc()),
                  FREE3_PROC,    new ProcMenuProc(new Free3Proc()),
                  MASTER_PROC,   new MasterDownProc(),
              }, new DisplayString(1, 1, "エラー")),

              // 各処理ループを抜けたら、選択メニューへ
              new Assign(procNoReg, 0)
          )
      );
      ctx.releaseReg(procNoReg);
      ctx.releaseReg(statusReg);
      systemTable.releaseReg(ctx);
    }
  }
}
