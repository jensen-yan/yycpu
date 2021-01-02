
package yycore

import chisel3._
import chisel3.iotesters.PeekPokeTester
import CoreTop.Top


class CoreTest(c: Top) extends PeekPokeTester(c){
  private val top = c
  for(i <- 0 to 5){
//    poke(top.io.cacheIO.addr, i)
//    poke(top.io.cacheIO.en, 1)
    step(1)
    expect(top.io.success, 1)
  }

}

object CoreTestGen extends App{
  iotesters.Driver.execute(args, () => new Top){
    c => new CoreTest(c)
  }
}

