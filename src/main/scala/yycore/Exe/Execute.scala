
package yycore.Exe

import chisel3._
import common.Constants._
import common.Instructions
import yycore.CoreTop._


class ExecuteIO extends Bundle{
  val ctrl = Flipped(new ControlIO)
  val regFileR = Flipped(new RegFileRIO)

  val fe_to_ex = Input(new Fe_to_ex)

  val ex_to_wb = Output(new Ex_to_wb)
  val ex_to_fe = Output(new Ex_to_fe)
}

class Execute extends Module{
  val io = IO(new ExecuteIO)
  val immGen  = Module(new ImmGen)
  val alu     = Module(new ALU)
  val brCond  = Module(new BrCond)

  /***** Execute / Write Back Registers *****/
  val wb_inst = RegInit(Instructions.NOP)
  val wb_pc   = Reg(UInt(xlen.W))
  val wb_alu  = Reg(UInt(xlen.W))
  // 控制reg
  val wb_sel  = Reg(Bool())
  val wb_wen  = Reg(Bool())

  // bus
  val ex_inst = Wire(UInt(inst_len.W))
  val ex_pc   = Wire(UInt(xlen.W))
  ex_inst := io.fe_to_ex.ex_inst
  ex_pc   := io.fe_to_ex.ex_pc

  /***** Execute *****/
  // decode
  io.ctrl.inst  := ex_inst

  // regfile Read
  val rs1_addr  = Wire(UInt(5.W))
  val rs2_addr  = Wire(UInt(5.W))
  val rd_addr   = Wire(UInt(5.W))
  rs1_addr := ex_inst(19, 15)
  rs2_addr := ex_inst(24, 20)
  rd_addr  := ex_inst(11, 7)

  io.regFileR.addr1 := rs1_addr
  io.regFileR.addr2 := rs2_addr

  // immGen
  immGen.io.inst  := ex_inst
  immGen.io.sel   := io.ctrl.imm_sel

  // bypass TODO
  val rs1 = Wire(UInt(xlen.W))
  val rs2 = Wire(UInt(xlen.W))
  rs1 := io.regFileR.data1
  rs2 := io.regFileR.data2

  //ALU
  alu.io.A := Mux(io.ctrl.A_sel === A_RS1, rs1, ex_pc)
  alu.io.B := Mux(io.ctrl.B_sel === B_RS2, rs2, immGen.io.out)
  alu.io.alu_op := io.ctrl.alu_op

  // BrCond
  brCond.io.rs1 := rs1
  brCond.io.rs2 := rs2
  brCond.io.br_type := io.ctrl.br_type
//   brCond.io.taken --> if: pc_sel

  // data TODO

  when(true.B){
    wb_inst := ex_inst
    wb_pc   := ex_pc
    wb_alu  := alu.io.out

    wb_sel  := io.ctrl.wb_sel   // 控制信号也要传递, 保证是ex这一拍译码成功的信号来控制这一拍的逻辑
    wb_wen  := io.ctrl.wb_en
  }

  // to wb bus
  io.ex_to_wb.wb_inst := wb_inst
  io.ex_to_wb.wb_pc   := wb_pc
  io.ex_to_wb.wb_alu  := wb_alu
  io.ex_to_wb.wb_sel  := wb_sel
  io.ex_to_wb.wb_wen  := wb_wen

  // to fs bus
  io.ex_to_fe.br_taken := brCond.io.taken

  if(DEBUG_P){
    printf("EX : pc=[%x] inst=[%x] alu=[%x]\n", ex_pc, ex_inst, alu.io.out)
  }

}
