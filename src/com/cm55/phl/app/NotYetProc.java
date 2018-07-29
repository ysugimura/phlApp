// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * 未作成処理
 */
public class NotYetProc extends Macro {
  @Override
  protected void expand(Context ctx) {
    Register keyReg = ctx.intReg();
    ctx.proc(
        new DisplayClear(),
        new DisplayString(3, 0, "未作成です"),
        new NoEchoInput(keyReg)
    );
    ctx.releaseReg(keyReg);
  }
}
