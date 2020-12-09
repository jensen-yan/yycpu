
package gcd

import chisel3._

object GCD2Main extends App {
  iotesters.Driver.execute(args, () => new GCD2(16) ){
    c => new GCDUnitTest2(c)
  }
}
