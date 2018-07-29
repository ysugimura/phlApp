// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class IncomingProc extends AbstractProc {

  public String getProcname() {
    return "入荷";
  }

  public Filename getFilename() {
    return new Filename(IncomingTable.FILENAME);
  }

  public Macro getInputProc() {
    return new IncomingInputProc();
  }

  public static class IncomingInputProc extends InputProc.CountType<IncomingTable,AppScreen.CountType<?>> {

    public IncomingInputProc() {
      IncomingTable table = new IncomingTable();
      init(table, new AppScreen.CountType<AppTable.CountType>(table, true));
    }
  }

}
