// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public class MoveProc extends AbstractProc {

  public String getProcname() {
    return "移動";
  }

  public Filename getFilename() {
    return new Filename(MoveTable.FILENAME);
  }

  public Macro getInputProc() {
    return new MoveInputProc();
  }

  public static class MoveInputProc extends
    InputProc.SrcDstType<MoveTable, MoveInputProc.MoveEditScreen> {

    private static class MoveEditScreen extends AppScreen.SrcDstType<MoveTable> {
      private MoveEditScreen(MoveTable table) {
        super(table, true);
      }
    }

    public MoveInputProc() {
      MoveTable table = new MoveTable();
      init(table, new MoveEditScreen(table));
    }
  }
}
