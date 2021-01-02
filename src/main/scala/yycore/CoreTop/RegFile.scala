
package yycore.CoreTop

import chisel3._
import common.Constants._

class RegFileRIO extends Bundle{
  val addr1    = Input(UInt(addr_w.W))
  val addr2    = Input(UInt(addr_w.W))
  val data1    = Output(UInt(xlen.W))
  val data2    = Output(UInt(xlen.W))
}

class RegFileWIO extends Bundle{
  val addr     = Input(UInt(addr_w.W))
  val data     = Input(UInt(xlen.W))
  val en       = Input(Bool())
}

class RegFileIO extends Bundle{
  // read port
  val r = new RegFileRIO
  val w = new RegFileWIO
//  val raddr1    = Input(UInt(addr_w.W))
//  val raddr2    = Input(UInt(addr_w.W))
//  val rdata1    = Output(UInt(xlen.W))
//  val rdata2    = Output(UInt(xlen.W))
//  // write port
//  val waddr     = Input(UInt(addr_w.W))
//  val wdata     = Input(UInt(xlen.W))
//  val wen       = Input(Bool())
}

class RegFile extends Module{
  val io = IO(new RegFileIO)
  val regs = Mem(32, UInt(xlen.W))    // 32个64位的
  io.r.data1   := Mux(io.r.addr1 === 0.U, 0.U, regs(io.r.addr1))
  io.r.data2   := Mux(io.r.addr2 === 0.U, 0.U, regs(io.r.addr2))
  when(io.w.en && io.w.addr =/= 0.U){
    regs(io.w.addr)  := io.w.data
  }

  if(DEBUG_P){
    printf("RF : r1=[%x]\n", regs(1))
  }
}
