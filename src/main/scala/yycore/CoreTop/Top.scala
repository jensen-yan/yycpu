
package yycore.CoreTop

import _root_.yycore.Cache.ICache
import chisel3._

class TopIO extends Bundle{
  val success = Output(Bool())
//  val cacheIO = new ICacheIO
}

class Top extends Module{
  val io = IO(new TopIO)

  val datapath  = Module(new Datapath)
  val iCache    = Module(new ICache)   // 必须加module

  datapath.io.icache <> iCache.io

  io.success := true.B
}
