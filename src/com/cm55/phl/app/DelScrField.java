// Created by Cryptomedia Co., Ltd. 2006/07/23
package com.cm55.phl.app;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;
import com.cm55.phl.gen.Screen.*;
import com.cm55.phl.gen.Table.*;

/**
 * 削除スクリーンフィールド
 */
public class DelScrField extends ScrField {

  public TblField recField;
  protected int y;
  protected int x;

  public DelScrField(int y, int x, TblField recField) {
    this.recField = recField;
    this.y = y;
    this.x = x;
  }

  @Override
  public Object clearElement() {
    return new DisplayPartClear(y, x, 6);
  }

  @Override
  public Object displayElement() {
    return new Macro() {
      @Override
      public void expand(Context ctx) {
        Register validReg = ctx.strReg();
        ctx.proc(
            recField.getValueElement(validReg),
            new If(validReg, Comp.NE, "*",
                new DisplayPartClear(y, x, 6),
                new DisplayString(y, x, "削除済")
            )
        );
        ctx.releaseReg(validReg);
      }
    };
  }
}
