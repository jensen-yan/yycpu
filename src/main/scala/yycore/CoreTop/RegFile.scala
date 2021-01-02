
package yycore.CoreTop

import chisel3._
import common.Constants._

class RegFileIO extends Bundle{
  // read port
  val raddr1    = Input(UInt(addr_w.W))
  val raddr2    = Input(UInt(addr_w.W))
  val rdata1    = Output(UInt(xlen.W))
  val rdata2    = Output(UInt(xlen.W))
  // write port
  val waddr     = Input(UInt(addr_w.W))
  val wdata     = Input(UInt(xlen.W))
  val wen       = Input(Bool())
}

class RegFile extends Module{
  val io = IO(new RegFileIO)
  val regs = Mem(32, UInt(xlen.W))    // 32个64位的
  io.rdata1   := Mux(io.raddr1 === 0.U, 0.U, regs(io.raddr1))
  io.rdata2   := Mux(io.raddr2 === 0.U, 0.U, regs(io.raddr2))
  when(io.wen && io.waddr =/= 0.U){
    regs(io.waddr)  := io.wdata
  }

  if(DEBUG_P){
    printf("RF : r1=[%x]\n", regs(1))
  }
}
