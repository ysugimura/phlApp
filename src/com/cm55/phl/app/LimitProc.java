package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * 返品処理
 * 
 * @author Yuichi Sugimura
 */
public class LimitProc extends AbstractProc {

  /** 処理名称を取得する */
  @Override
  public String getProcname() {
    return "期限";
  }

  /** ファイル名称を取得する */
  @Override
  public Filename getFilename() {
    return new Filename(LimitTable.FILENAME);
  }

  /** 入力処理を取得する */
  @Override
  public Macro getInputProc() {
    return new LimitInputProc();
  }

  /** 入力処理 */
  private static class LimitInputProc
    extends InputProc.DepotLimitType<LimitTable,AppScreen.DepotLimitType<?>> {

    public LimitInputProc() {
      LimitTable table = new LimitTable();
      init(table, new AppScreen.DepotLimitType<AppTable.DepotLimitType>(table, true));
    }
  }
}
