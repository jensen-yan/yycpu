
package yycore

import chisel3._
import chisel3.iotesters.PeekPokeTester
import yycore.CoreTop.RegFile


class RegFileTest(c: RegFile) extends PeekPokeTester(c){
  private val rf = c
  for(i <- 0 to 20){
    val addr = rnd.nextInt(64)
    val data = rnd.nextInt(10000)
    // write
    poke(rf.io.w.addr, addr)
    poke(rf.io.w.data, data)
    poke(rf.io.w.en, 1)
    step(1)
    // read
    // input
    poke(rf.io.r.addr2, addr)
    // exe

    // out
    expect(rf.io.r.data2, data)
  }

}

object RegFileTestGen extends App{
  iotesters.Driver.execute(args, () => new RegFile){
    c => new RegFileTest(c)
  }
}
