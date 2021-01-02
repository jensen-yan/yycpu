
package yycore.End

import chisel3._
import common.Constants._
import chisel3.util.MuxLookup
import yycore.CoreTop.{Ex_to_wb, RegFile}

class WriteIO extends Bundle{
  val ex_to_wb = Input(new Ex_to_wb)
}



class Wirte extends Module{
  val io = IO(new WriteIO)

  val regFile = Module(new RegFile)
  regFile.io.raddr1   := DontCare
  regFile.io.raddr2   := DontCare


  // from ex bus
  val ex_inst = io.ex_to_wb.ex_inst
  val ex_alu  = io.ex_to_wb.ex_alu
  val ex_pc   = io.ex_to_wb.ex_pc
  val wb_sel  = io.ex_to_wb.wb_sel
  val wb_wen  = io.ex_to_wb.wb_wen

  val regWrite = MuxLookup(wb_sel, ex_alu, Seq(
    WB_PC4  -> (ex_pc + 4.U)
  ))

  regFile.io.wen   := wb_wen
  regFile.io.waddr := ex_inst(11, 7)
  regFile.io.wdata := regWrite

//  if(DEBUG_P){
//    printf("WE : pc=[%x] inst=[%x] alu=[%x]\n", ex_pc, ex_inst, ex_alu)
//  }
}
