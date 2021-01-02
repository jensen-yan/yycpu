
package yycore.CoreTop

import chisel3._
import chisel3.util._
import common.CSR
import common.Constants._
import common.Instructions._

class ControlIO extends Bundle{
  val inst      = Input(UInt(xlen.W))
  val pc_sel    = Output(UInt(pc_sel_w.W))
  val inst_kill = Output(Bool())
  val A_sel     = Output(UInt(A_sel_w.W))
  val B_sel     = Output(UInt(B_sel_w.W))
  val imm_sel   = Output(UInt(imm_sel_w.W))
  val alu_op    = Output(UInt(op_w.W))
  val br_type   = Output(UInt(br_type_w.W))
  val st_type   = Output(UInt(st_type_w.W))
  val ld_type   = Output(UInt(ld_type_w.W))
  val wb_sel    = Output(UInt(wb_sel_w.W))
  val wb_en     = Output(Bool())
  val csr_cmd   = Output(UInt(3.W))
  val illegal   = Output(Bool())
}

class Control extends Module{
  val io = IO(new ControlIO)

  val default =
  //                                                            kill                        wb_en  illegal?
  //            pc_sel  A_sel   B_sel  imm_sel   alu_op   br_type |  st_type ld_type wb_sel  | csr_cmd |
  //              |       |       |     |          |          |   |     |       |       |    |  |      |
            List(PC_4,   A_XXX,  B_XXX, IMM_X, ALU_XXX   , BR_XXX, N, ST_XXX, LD_XXX, WB_ALU, N, CSR.N, Y)
  val map = Array(
    LUI  -> List(PC_4  , A_XXX,  B_IMM, IMM_U, ALU_COPY_B, BR_XXX, N, ST_XXX, LD_XXX, WB_ALU, Y, CSR.N, N),
    ADD  -> List(PC_4,   A_RS1,  B_RS2, IMM_X, ALU_ADD   , BR_XXX, N, ST_XXX, LD_XXX, WB_ALU, Y, CSR.N, N)
  )

  val ctrlSignals = ListLookup(io.inst, default, map)

  // Control signals for Fetch
  io.pc_sel    := ctrlSignals(0)
  io.inst_kill := ctrlSignals(6).toBool   // jal, jalr, load会有kill?

  // Control signals for Execute 执行相关
  io.A_sel   := ctrlSignals(1)
  io.B_sel   := ctrlSignals(2)
  io.imm_sel := ctrlSignals(3)
  io.alu_op  := ctrlSignals(4)
  io.br_type := ctrlSignals(5)
  io.st_type := ctrlSignals(7)

  // Control signals for Write Back 写回相关
  io.ld_type := ctrlSignals(8)
  io.wb_sel  := ctrlSignals(9)
  io.wb_en   := ctrlSignals(10).toBool
  io.csr_cmd := ctrlSignals(11)
  io.illegal := ctrlSignals(12)



}
