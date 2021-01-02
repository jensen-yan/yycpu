
package yycore.Exe

import common.Constants._
import chisel3._

class BrCondIO extends Bundle{
  val rs1     = Input(UInt(xlen.W))
  val rs2     = Input(UInt(xlen.W))
  val br_type = Input(UInt(br_type_w.W))
  val taken   = Output(Bool())
}

class BrCond extends Module{
  val io = IO(new BrCondIO)

  val eq    = io.rs1 === io.rs2
  val neq   = !eq
  val lt    = io.rs1.asSInt() < io.rs2.asSInt()
  val ge    = !lt
  val ltu   = io.rs1 < io.rs2
  val geu   = !ltu

  io.taken := ((io.br_type === BR_EQ) && eq)  ||
              ((io.br_type === BR_NE) && neq) ||
              ((io.br_type === BR_LT) && lt)  ||
              ((io.br_type === BR_GE) && ge)  ||
              ((io.br_type === BR_LTU) && ltu)||
              ((io.br_type === BR_GEU) && geu)
}
