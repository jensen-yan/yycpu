
package yycore.End

import chisel3._
import common.Constants._
import chisel3.util.MuxLookup
import yycore.CoreTop.{Ex_to_wb, RegFileWIO}

class WriteIO extends Bundle{
  val ex_to_wb = Input(new Ex_to_wb)
  val regFileW = Flipped(new RegFileWIO)
}

class Wirte extends Module{
  val io = IO(new WriteIO)


  // from ex bus
  val wb_pc   = io.ex_to_wb.wb_pc
  val wb_inst = io.ex_to_wb.wb_inst
  val wb_alu  = io.ex_to_wb.wb_alu
  val wb_sel  = io.ex_to_wb.wb_sel
  val wb_wen  = io.ex_to_wb.wb_wen

  val regWrite = MuxLookup(wb_sel, wb_alu, Seq(
    WB_PC4  -> (wb_pc + 4.U)
  ))

  io.regFileW.en   := wb_wen
  io.regFileW.addr := wb_inst(11, 7)
  io.regFileW.data := regWrite

  if(DEBUG_P){
    printf("WE : pc=[%x] inst=[%x] alu=[%x]\n", wb_pc, wb_inst, wb_alu)
  }
}
