
package common

import chisel3._
import chisel3.util._

object Constants extends
  Common_constants with
  ALU_constans with
  Control_constants
{}

trait Common_constants {
  val xlen = 32         // todo
  val inst_len = 32     // 无论32/64位, 指令长度都是32!

//  val PC_START = 0x80000000 - 4
  val PC_START = 0    // todo

  val DEBUG_P = true

  val addr_w = log2Ceil(xlen)
}


trait ALU_constans{
  val op_w = 4
  val ALU_ADD    = 0.U(op_w.W)
  val ALU_SUB    = 1.U(op_w.W)
  val ALU_AND    = 2.U(op_w.W)
  val ALU_OR     = 3.U(op_w.W)
  val ALU_XOR    = 4.U(op_w.W)
  val ALU_SLT    = 5.U(op_w.W)
  val ALU_SLL    = 6.U(op_w.W)
  val ALU_SLTU   = 7.U(op_w.W)
  val ALU_SRL    = 8.U(op_w.W)
  val ALU_SRA    = 9.U(op_w.W)
  val ALU_COPY_A = 10.U(op_w.W)
  val ALU_COPY_B = 11.U(op_w.W)
  val ALU_XXX    = 15.U(op_w.W)
}

trait Control_constants{
  val Y = true.B
  val N = false.B

  // pc_sel
  val pc_sel_w = 2
  val PC_4   = 0.U(pc_sel_w.W)
  val PC_ALU = 1.U(pc_sel_w.W)
  val PC_0   = 2.U(pc_sel_w.W)
  val PC_EPC = 3.U(pc_sel_w.W)

  // A_sel
  val A_sel_w = 1
  val A_XXX  = 0.U(A_sel_w.W)
  val A_PC   = 0.U(A_sel_w.W)
  val A_RS1  = 1.U(A_sel_w.W)

  // B_sel
  val B_sel_w = 1
  val B_XXX  = 0.U(B_sel_w.W)
  val B_IMM  = 0.U(B_sel_w.W)
  val B_RS2  = 1.U(B_sel_w.W)

  // imm_sel
  val imm_sel_w = 3
  val IMM_X  = 0.U(imm_sel_w.W)
  val IMM_I  = 1.U(imm_sel_w.W)
  val IMM_S  = 2.U(imm_sel_w.W)
  val IMM_U  = 3.U(imm_sel_w.W)
  val IMM_J  = 4.U(imm_sel_w.W)
  val IMM_B  = 5.U(imm_sel_w.W)
  val IMM_Z  = 6.U(imm_sel_w.W)

  // br_type
  val br_type_w = 3
  val BR_XXX = 0.U(br_type_w.W)
  val BR_LTU = 1.U(br_type_w.W)
  val BR_LT  = 2.U(br_type_w.W)
  val BR_EQ  = 3.U(br_type_w.W)
  val BR_GEU = 4.U(br_type_w.W)
  val BR_GE  = 5.U(br_type_w.W)
  val BR_NE  = 6.U(br_type_w.W)

  // st_type
  val st_type_w = 2
  val ST_XXX = 0.U(st_type_w.W)
  val ST_SW  = 1.U(st_type_w.W)
  val ST_SH  = 2.U(st_type_w.W)
  val ST_SB  = 3.U(st_type_w.W)

  // ld_type
  val ld_type_w = 3
  val LD_XXX = 0.U(ld_type_w.W)
  val LD_LW  = 1.U(ld_type_w.W)
  val LD_LH  = 2.U(ld_type_w.W)
  val LD_LB  = 3.U(ld_type_w.W)
  val LD_LHU = 4.U(ld_type_w.W)
  val LD_LBU = 5.U(ld_type_w.W)

  // wb_sel
  val wb_sel_w = 2
  val WB_ALU = 0.U(wb_sel_w.W)
  val WB_MEM = 1.U(wb_sel_w.W)
  val WB_PC4 = 2.U(wb_sel_w.W)
  val WB_CSR = 3.U(wb_sel_w.W)

}

object CSR{
  val N = 0.U(3.W)
}
