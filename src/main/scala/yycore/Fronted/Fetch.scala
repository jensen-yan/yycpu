
package yycore.Fronted

import common.Constants._
import chisel3._
import common.Instructions
import yycore.Cache.ICacheIO
import yycore.CoreTop.Fe_to_ex

class FetchIO extends Bundle{
  val icache = Flipped(new ICacheIO)
  val fe_to_ex = Output(new Fe_to_ex)
}


class Fetch extends Module{
  val io = IO(new FetchIO)


  val ex_inst = RegInit(Instructions.NOP)     // ex级对应的pc, inst
  val ex_pc   = Reg(UInt())


  val pc      = RegInit(PC_START.U(xlen.W))   // 当前拍的pc
  val npc     = Wire(UInt(xlen.W))
  val inst    = Wire(UInt(inst_len.W))

  npc     := pc + 4.U  // todo
  pc      := npc

  inst    := io.icache.data(31,0)   // 只要低32位
  io.icache.addr  := npc
  io.icache.en    := true.B

  when(true.B){
    ex_pc   := pc
    ex_inst := inst
  }

  // bus
  io.fe_to_ex.ex_pc   := ex_pc
  io.fe_to_ex.ex_inst := ex_inst

  if(DEBUG_P){
    printf("IF : pc=[%x] inst=[%x]\n", pc, inst)
  }

}
