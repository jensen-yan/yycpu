
package gcd

import chisel3.iotesters._

class GCDUnitTest2(c: GCD2) extends PeekPokeTester(c) {

  def computeGcd(a:Int, b:Int) :(Int, Int) = {
    var x = a
    var y = b
    var depth = 1
    while(y > 0){
      if(x > y) {
        x = x - y
      }
      else {
        y = y - x
      }
      depth += 1
    }
    (x, depth)
  }

  private val gcd = c

  for (i <- 1 to 20 by 3){
    for (j <- 1 to 20 by 7){
      // input
//      val i = 4
//      val j = 15
      poke(gcd.io.v1, i)
      poke(gcd.io.v2, j)
      poke(gcd.io.load, true)
      step(1)
      poke(gcd.io.load, false)
      // exe
      val (out, steps) = computeGcd(i, j)
      step(steps - 1)
      // out
      expect(gcd.io.out, out)
      expect(gcd.io.valid, 1)
    }
  }
}
