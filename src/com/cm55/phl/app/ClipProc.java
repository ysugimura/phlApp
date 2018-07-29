package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class ClipProc extends AbstractProc {

  public String getProcname() {
    return "ｸﾘｯﾌﾟ";
  }

  public Filename getFilename() {
    return new Filename(ClipTable.FILENAME);
  }

  public Macro getInputProc() {
    return new ClipInputProc();
  }
  
  @SuppressWarnings("rawtypes")
  public static class ClipInputProc extends InputProc<ClipTable,AppScreen> {
    public ClipInputProc() {
      ClipTable table = new ClipTable();
      init(table, new AppScreen<AppTable>(table, true));
    }
  }
}
