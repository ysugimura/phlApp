package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;


public class Free3Proc extends AbstractProc {

  public String getProcname() {
    return "自由3";
  }

  public Filename getFilename() {
    return new Filename(Free3Table.FILENAME);
  }

  public Macro getInputProc() {
    return new Free3InputProc();
  }

  public static class Free3InputProc extends InputProc.Free3Type<Free3Table,AppScreen.Free3Type<?>> {

    public Free3InputProc() {
      Free3Table table = new Free3Table();
      init(table, new AppScreen.Free3Type<AppTable.Free3Type>(table, true));
    }
  }

}
