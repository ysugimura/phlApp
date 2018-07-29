// Created by Cryptomedia Co., Ltd. 2006/06/11
package com.cm55.phl.app;

import static com.cm55.phl.app.Consts.*;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/////////////////////////////////////////////////////////////////////////////
// ファイル送信処理
/////////////////////////////////////////////////////////////////////////////

class UploadProc extends Macro {

  String procname;
  Filename filename;

  UploadProc(String procname, Filename filename) {
    this.procname = procname;
    this.filename = filename;
  }

  @Override
  protected void expand(Context ctx) {
    Register keyInValue = ctx.intReg();
    ctx.proc(
        //new InfiniteLoop(
          // ファイル送信
          new InteractiveFileUpload(keyInValue, filename)
            .setMessage(new SJIS_DAT(procname + "ﾌｧｲﾙ")),

          // 送信失敗、メニューに戻る
          new IfContinue(keyInValue, Comp.NE, 0),

          // 成功した場合、削除するかを聞く
          new DisplayString(LINE1, 0, "送信済ﾌｧｲﾙを"),
          new DisplayString(LINE2, 0, "削除しますか?"),
          new DisplayString(LINE3, 0, "ENT:Y"),
          new NoEchoInput(keyInValue),
          new IfContinue(keyInValue, Comp.NE, Key.ENT),

          // 削除
          new InteractiveFileDelete(keyInValue, filename)
        //)
    );
    ctx.releaseReg(keyInValue);
  }
}

