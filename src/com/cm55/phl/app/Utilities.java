// Created by Cryptomedia Co., Ltd. 2006/09/19
package com.cm55.phl.app;

import static com.cm55.phl.app.Consts.*;

import com.cm55.phl.Command.*;
import com.cm55.phl.gen.*;

public class Utilities {

  /** マスタ検索なし */
  public static class NoMasterSearch extends Macro {
    protected void expand(Context ctx) {
      ctx.proc(
          new DisplayString(LINE2, 0, "[表示不可]").setClear(16)
      );
    }
  }
}
