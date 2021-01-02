
package yycore


import chisel3._
import chisel3.iotesters.PeekPokeTester
import Cache.ICache


class CacheTest(c: ICache) extends PeekPokeTester(c){
  private val cache = c
  for(i <- 0 to 20){
    val addr = i
    val data = rnd.nextInt(10000)
    // read
    poke(cache.io.addr, addr)
    step(1)

  }

}

object CacheTestGen extends App{
  iotesters.Driver.execute(args, () => new ICache){
    c => new CacheTest(c)
  }
}
