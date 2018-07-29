// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import static com.cm55.phl.app.Consts.*;

import java.util.*;

import com.cm55.phl.Command.*;
import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * 売価表示処理
 */
public class PriceProc extends Macro {

  @Override
  protected void expand(Context ctx) {
    ctx.proc(
        new MenuLoop(
            new String[] {
                "1.売価表示",
                "2.メインヘ",
            },
            new Object[] {
                new DoProc(),
                new Break()
            },
            null,null
         )
    );
  }

  private static class DoProc extends Macro {
    protected MasterTable masterTable = new MasterTable();

    @Override
    protected void expand(Context ctx) {
      Register barReg = ctx.strReg();
      EnumSet<Key>special = Key.getKeySet(
          new Key[] { QUIT_KEY }
      );
      masterTable.allocReg(ctx);
      DisplayRegister a;
      ctx.proc(
          new DisplayClear(),
          new DisplayString(LINE0, 0, "J[             ]"),
          new DisplayString(LINE1, 0, "J[             ]"),
          new InfiniteLoop(
              new BarcodeInput(LINE0, 2, barReg, 13, special).
                setFullAction(FullAction.IMMEDIATE),
              new IfBreak(Register.RSLT, Comp.NE, 0),
              new DisplayPartClear(LINE0, 2, 13),
              new DisplayRegister(LINE1, 2, barReg, 0, 13),
              masterTable.searchElement(masterTable.barcode, barReg),
              new If(Register.RSLT, Comp.NE, 0, new Compound(
                  new DisplayString(LINE2, 0, "[商品不明]").setClear(16),
                  new Continue()
              )),
              masterTable.name.displayElement(LINE2, 0),
              masterTable.price.displayElement(LINE3, 9)
          )
      );
      ctx.releaseReg(barReg);
      masterTable.releaseReg(ctx);
    }
  }
}
