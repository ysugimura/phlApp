// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * 一般的な処理のメニューひながた
 */
public class ProcMenuProc extends Macro {

//  /** 処理名称 */
//  protected String procname;
//
//  /** ファイル名称 */
//  protected Filename filename;
//
//  /** 入力処理 */
//  protected Macro inputProc;

  protected AbstractProc proc;

  /** 作成する */
  protected ProcMenuProc(AbstractProc proc) {
    this.proc = proc;

//    this.procname = procname;
//    this.filename = new Filename(filename);
//    this.inputProc = inputProc;
  }

  /** マクロ展開 */
  @Override
  protected void expand(Context ctx) {
    Register existsReg = ctx.intReg(); // ファイル存在
    Register sendTitle = ctx.strReg(); // 送信タイトル
    Register deleteTitle = ctx.strReg(); // 削除タイトル
    Register keyReg = ctx.intReg(); //キー入力
    ctx.proc(
        new InfiniteLoop(
            // ファイル存在を取得、タイトル設定
            new Assign(existsReg, 0),
            new Assign(sendTitle,   "2.ﾌｧｲﾙなし"),
            new Assign(deleteTitle, "3.ﾌｧｲﾙなし"),
            new IfFileExists(proc.getFilename(), new Compound(
                new Assign(existsReg, 1),
                new Assign(sendTitle,   "2." + proc.getProcname() + "ﾃﾞｰﾀ送信"),
                new Assign(deleteTitle, "3." + proc.getProcname() + "ﾌｧｲﾙ削除")
            )),

            // メニュー表示・キー入力
            new MenuLines(new Object[] {
//                "　［" + procname + "処理］",
                "1." + proc.getProcname() + "入力",
                sendTitle,
                deleteTitle,
                "4.メインヘ",
            }),
            new NoEchoInput(keyReg),

            // 処理ふりわけ
            new Case(keyReg, new Object[] {
                Key.One, new Compound(
                    proc.getInputProc(),
                    new Continue()
                ),
                Key.Four, new Break(),
            }),
            new IfContinue(existsReg, 0),
            new Case(keyReg, new Object[] {
                Key.Two, new UploadProc(proc.getProcname(), proc.getFilename()),
                Key.Three, new InteractiveFileDelete(keyReg, proc.getFilename())
            })
        )
    );
    ctx.releaseReg(existsReg, sendTitle, deleteTitle, keyReg);
  }

}
