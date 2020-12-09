
package gcd

import chisel3._

class GCD2IO(val w:Int) extends Bundle{
  val v1       = Input(UInt(w.W))
  val v2       = Input(UInt(w.W))
  val load    = Input(Bool())
  val out     = Output(UInt(w.W))
  val valid   = Output(Bool())
}

class GCD2(val w: Int) extends Module{
  val io = IO(new GCD2IO(w))

  val x = Reg(UInt(w.W))
  val y = Reg(UInt(w.W))
  // load
  // 尽量不要用两个并列的when! 除非保证一定互斥! 防止chisel生成奇怪的代码!
  // 尽量用.elsewhen!
  when(io.load){
    x := io.v1
    y := io.v2
  }
  // 运算
  .elsewhen(x > y){
    x := x - y
  }.otherwise{
    y := y - x
  }
  // 输出
  io.out := x
  io.valid := y === 0.U
}
