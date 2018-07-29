// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class StockProc extends AbstractProc {

  public String getProcname() {
    return "棚卸";
  }

  public Filename getFilename() {
    return new Filename(StockTable.FILENAME);
  }

  public Macro getInputProc() {
    return new StockInputProc();
  }

  public static class StockInputProc extends
  InputProc.LocCntType<StockTable, AppScreen.LocCntType<?>> {

    /** 作成 */
    public StockInputProc() {
      StockTable table = new StockTable();
      init(table, new StockEditScreen(table));
    }

    /** 棚卸データ入力用画面 */
    private class StockEditScreen extends AppScreen.LocCntType<StockTable> {
      private StockEditScreen(StockTable stockTable) {
        super(stockTable, true);
      }
    }

  }

}
