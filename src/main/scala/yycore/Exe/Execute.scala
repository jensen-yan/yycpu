
package yycore.Exe

import chisel3._
import common.Constants._
import common.Instructions
import yycore.CoreTop._


class ExecuteIO extends Bundle{
  val ctrl = Flipped(new ControlIO)

  val fe_to_ex = Input(new Fe_to_ex)

  val ex_to_wb = Output(new Ex_to_wb)
  val ex_to_fe = Output(new Ex_to_fe)
}

class Execute extends Module{
  val io = IO(new ExecuteIO)
  val regFile = Module(new RegFile)
  val immGen  = Module(new ImmGen)
  val alu     = Module(new ALU)
  val brCond  = Module(new BrCond)

  /***** Execute / Write Back Registers *****/
  val ex_inst = RegInit(Instructions.NOP)
  val ex_pc   = Reg(UInt(xlen.W))
  val ex_alu  = Reg(UInt(xlen.W))
  // 控制reg
  val wb_sel  = Reg(Bool())
  val wb_wen  = Reg(Bool())

  // bus
  val fe_inst = Wire(UInt(inst_len.W))
  val fe_pc   = Wire(UInt(xlen.W))
  fe_inst := io.fe_to_ex.fe_inst
  fe_pc   := io.fe_to_ex.fe_pc

  /***** Execute *****/
  // decode
  io.ctrl.inst  := fe_inst

  // regfile Read
  val rs1_addr  = Wire(UInt(5.W))
  val rs2_addr  = Wire(UInt(5.W))
  val rd_addr   = Wire(UInt(5.W))
  rs1_addr := fe_inst(19, 15)
  rs2_addr := fe_inst(24, 20)
  rd_addr  := fe_inst(11, 7)

  regFile.io.raddr1 := rs1_addr
  regFile.io.raddr2 := rs2_addr

  regFile.io.wen    := DontCare
  regFile.io.wdata  := DontCare
  regFile.io.waddr  := DontCare

  // immGen
  immGen.io.inst  := fe_inst
  immGen.io.sel   := io.ctrl.imm_sel

  // bypass TODO
  val rs1 = Wire(UInt(xlen.W))
  val rs2 = Wire(UInt(xlen.W))
  rs1 := regFile.io.rdata1
  rs2 := regFile.io.rdata2

  //ALU
  alu.io.A := Mux(io.ctrl.A_sel === A_RS1, rs1, fe_pc)
  alu.io.B := Mux(io.ctrl.B_sel === B_RS2, rs2, immGen.io.out)
  alu.io.alu_op := io.ctrl.alu_op

  // BrCond
  brCond.io.rs1 := rs1
  brCond.io.rs2 := rs2
  brCond.io.br_type := io.ctrl.br_type
//   brCond.io.taken --> if: pc_sel

  // data TODO

  when(true.B){
    ex_inst := fe_inst
    ex_pc   := fe_pc
    ex_alu  := alu.io.out

    wb_sel  := io.ctrl.wb_sel
    wb_wen  := io.ctrl.wb_en
  }

  // to wb bus
  io.ex_to_wb.ex_inst := ex_inst
  io.ex_to_wb.ex_pc   := ex_inst
  io.ex_to_wb.ex_alu  := ex_alu
  io.ex_to_wb.wb_sel  := wb_sel
  io.ex_to_wb.wb_wen  := wb_wen

  // to fs bus
  io.ex_to_fe.br_taken := brCond.io.taken

  if(DEBUG_P){
    printf("EX : pc=[%x] inst=[%x] alu=[%x]\n", ex_pc, ex_inst, ex_alu)
  }

}
