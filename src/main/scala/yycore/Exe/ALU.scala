
package yycore.Exe

import chisel3._
import chisel3.util._
import common.Constants._

class ALUIO extends Bundle(){
  val A     = Input(UInt(xlen.W))
  val B     = Input(UInt(xlen.W))
  val alu_op= Input(UInt(op_w.W))
  val out   = Output(UInt(xlen.W))
  val sum   = Output(UInt(xlen.W))
}

class ALU extends Module{
  val io = IO(new ALUIO)

  val shamt = io.B(4,0).asUInt()    // TODO: 取多少位?

  io.out  := MuxLookup(io.alu_op, io.B, Seq(
    ALU_ADD     ->  (io.A + io.B),
    ALU_SUB     ->  (io.A - io.B),
    ALU_COPY_B  ->   io.B
  ))

  io.sum  := io.A + Mux(io.alu_op(0), -io.B, io.B)

}
