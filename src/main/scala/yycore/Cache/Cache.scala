
package yycore.Cache

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile
import common.Constants._

class CacheReq extends Bundle{
  val addr  = UInt(xlen.W)
  val data  = UInt(xlen.W)
  val mask  = UInt((xlen >> 3).W)
}

class CacheResp extends Bundle{
  val data  = UInt(xlen.W)
}

//class CacheIO extends Bundle{
//  val addr  = Output(UInt(inst_len.W))
//  val en    = Output(Bool())
//  val data  = Input(UInt(xlen.W))
//}

class ICacheIO extends Bundle{
  val addr  = Input(UInt(inst_len.W))   // 输入地址!!
  val en    = Input(Bool())
  val data  = Output(UInt(inst_len.W))
}

// 指令cache
class ICache extends Module{
  val io = IO(new ICacheIO)

  val memory = Mem(100, UInt(inst_len.W)) // 存100个32位的pc
//  when(io.en){
//    io.data := memory(io.addr)
//  }

  io.data := memory(io.addr)

  loadMemoryFromFile(memory, "/home/yanyue/nutshell_v2/yycpu/src/main/scala/yycore/Cache/mem1.hex.txt")
//  loadMemoryFromFile(memory, "mem1.hex.txt")

  printf("data = %x\n", io.data)

}
