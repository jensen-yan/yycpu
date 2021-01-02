
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


  val fe_inst = RegInit(Instructions.NOP)
  val fe_pc   = Reg(UInt())


  val pc      = RegInit(PC_START.U(xlen.W))
  val npc     = Wire(UInt(xlen.W))
  val inst    = Wire(UInt(inst_len.W))

  npc     := pc + 4.U  // todo
  pc      := npc

  inst    := io.icache.data(31,0)   // 只要低32位
  io.icache.addr  := npc
  io.icache.en    := true.B

  when(true.B){
    fe_pc   := pc
    fe_inst := inst
  }

  // bus
  io.fe_to_ex.fe_pc   := fe_pc
  io.fe_to_ex.fe_inst := fe_inst

  if(DEBUG_P){
    printf("IF : pc=[%x] inst=[%x]\n", fe_pc, fe_inst)
  }

}
