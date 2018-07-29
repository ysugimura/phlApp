// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

/**
 * 返品処理
 * 
 * @author Yuichi Sugimura
 */
public class ReturnProc extends AbstractProc {

  /** 処理名称を取得する */
  @Override
  public String getProcname() {
    return "返品";
  }

  /** ファイル名称を取得する */
  @Override
  public Filename getFilename() {
    return new Filename(ReturnTable.FILENAME);
  }

  /** 入力処理を取得する */
  @Override
  public Macro getInputProc() {
    return new ReturnInputProc();
  }

  /** 入力処理 */
  private static class ReturnInputProc
    extends InputProc.DepotCntType<ReturnTable,AppScreen.DepotCntType<?>> {

    public ReturnInputProc() {
      ReturnTable table = new ReturnTable();
      init(table, new AppScreen.DepotCntType<AppTable.DepotCntType>(table, true));
    }
  }
}
