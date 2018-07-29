// Created by Cryptomedia Co., Ltd. 2006/09/26
package com.cm55.phl.app;

import com.cm55.phl.PHL.*;
import com.cm55.phl.gen.*;

public abstract class AbstractProc {

  /** 処理名 */
  public abstract String getProcname();

  /** ファイル名称 */
  public abstract Filename getFilename();

  /** 入力処理の取得 */
  public abstract Macro getInputProc();

}
