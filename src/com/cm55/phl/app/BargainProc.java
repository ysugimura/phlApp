// Created by Cryptomedia Co., Ltd. 2006/09/27
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class BargainProc extends AbstractProc {

  public String getProcname() {
    return "特売";
  }

  public Filename getFilename() {
    return new Filename(BargainTable.FILENAME);
  }

  public Macro getInputProc() {
    return new BargainInputProc();
  }

  /*
  public static class BargainInputProc extends InputProc<BargainTable,AppScreen> {
    public BargainInputProc() {
      BargainTable table = new BargainTable();
      init(table, new AppScreen<AppTable>(table, true));
    }
  }
  */
  public static class BargainInputProc extends InputProc.PriceType<BargainTable,AppScreen.PriceType<?>> {

    public BargainInputProc() {
      BargainTable table = new BargainTable();
      init(table, new AppScreen.PriceType<AppTable.PriceType>(table, true));
    }
  }
}
