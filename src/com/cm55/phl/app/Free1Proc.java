package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class Free1Proc extends AbstractProc {

  public String getProcname() {
    return "自由1";
  }

  public Filename getFilename() {
    return new Filename(Free1Table.FILENAME);
  }

  public Macro getInputProc() {
    return new Free1InputProc();
  }

  public static class Free1InputProc extends InputProc.Free1Type<Free1Table,AppScreen.Free1Type<?>> {
    public Free1InputProc() { 
      Free1Table table = new Free1Table();
      init(table, new AppScreen.Free1Type<AppTable.Free1Type>(table, true));
    }
  }

}
