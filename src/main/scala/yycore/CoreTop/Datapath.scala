
package yycore.CoreTop

import chisel3._
import common.Constants._
import yycore.Cache._
import yycore.Fronted.Fetch
import yycore.End.Wirte
import yycore.Exe.Execute



class Fe_to_ex extends Bundle{
  val fe_inst = UInt(inst_len.W)
  val fe_pc   = UInt(xlen.W)
}

class Ex_to_fe extends Bundle{
  val br_taken = UInt()
}

class Ex_to_wb extends Bundle{
  val ex_inst = UInt(inst_len.W)
  val ex_alu  = UInt(xlen.W)
  val ex_pc   = UInt(xlen.W)

  val wb_sel  = UInt()
  val wb_wen  = UInt()
}

class DatapathIO extends Bundle{
  val icache  = Flipped(new ICacheIO)

}

class Datapath extends Module{
  val io = IO(new DatapathIO)

  val fetch   = Module(new Fetch)
  val execute = Module(new Execute)
  val write   = Module(new Wirte)
  val ctrl    = Module(new Control)

  execute.io.ctrl <> ctrl.io
  fetch.io.icache <> io.icache

  // 三级流水线数据传输
  fetch.io.fe_to_ex <> execute.io.fe_to_ex
  execute.io.ex_to_wb <> write.io.ex_to_wb

}
