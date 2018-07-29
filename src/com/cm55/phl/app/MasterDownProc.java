// Created by Cryptomedia Co., Ltd. 2006/06/12
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * ファイルダウンロード処理
 */
public class MasterDownProc extends Macro {

  @Override
  protected void expand(Context ctx) {
    Register inputReg = ctx.intReg();
    ctx.proc(
        new MenuLoop(
            new String[] {
                "1.マスタ受信",
                "2.メインヘ",
            },
            new Object[] {
                new InteractiveFileDownload(inputReg)
                  .setMessage(new SJIS_DAT("マスタ受信")),
                new Break()
            },
            null,null
         )
    );
    ctx.releaseReg(inputReg);
  }
}
