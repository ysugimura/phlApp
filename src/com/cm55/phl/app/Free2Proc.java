package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;


public class Free2Proc extends AbstractProc {

  public String getProcname() {
    return "自由2";
  }

  public Filename getFilename() {
    return new Filename(Free2Table.FILENAME);
  }

  public Macro getInputProc() {
    return new Free2InputProc();
  }

  public static class Free2InputProc extends InputProc.Free2Type<Free2Table,AppScreen.Free2Type<?>> {

    public Free2InputProc() {
      Free2Table table = new Free2Table();
      init(table, new AppScreen.Free2Type<AppTable.Free2Type>(table, true));
    }
  }

}
