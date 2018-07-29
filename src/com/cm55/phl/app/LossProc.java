// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class LossProc extends AbstractProc {

  public String getProcname() {
    return "ロス";
  }

  public Filename getFilename() {
    return new Filename(LossTable.FILENAME);
  }

  public Macro getInputProc() {
    return new LossInputProc();
  }

  public static class LossInputProc extends InputProc.DepotCntType<LossTable,AppScreen.DepotCntType<?>> {
    public LossInputProc() {
      LossTable table = new LossTable();
      init(table, new AppScreen.DepotCntType<AppTable.DepotCntType>(table, true));
    }
  }

}
