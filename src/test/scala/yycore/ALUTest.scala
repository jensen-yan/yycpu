
package yycore

import chisel3._
import chisel3.iotesters.PeekPokeTester
import common.Constants._
import Exe.ALU


class ALUTest(c: ALU) extends PeekPokeTester(c){
  def add(a:Int, b:Int): Int = a + b
  def sub(a:Int, b:Int): Int = a - b

  private val alu = c

  for(i <- 1 to 10){
    for(j <- 1 to 10){
      val A = rnd.nextInt(200)
      val B = rnd.nextInt(200)

      //input
      poke(alu.io.A, A)
      poke(alu.io.B, B)
      poke(alu.io.alu_op, ALU_ADD)
      // cal
      val out = add(A, B)
      // out
      expect(alu.io.out, out)
      expect(alu.io.sum, out)

      //input
      poke(alu.io.A, A)
      poke(alu.io.B, B)
      poke(alu.io.alu_op, ALU_SUB)
      // cal
      val out2 = sub(A, B)
      // out
      expect(alu.io.out, out2)
      expect(alu.io.sum, out2)
    }
  }




}

object ALUTestGen extends App{
  iotesters.Driver.execute(args, () => new ALU) {
    c => new ALUTest(c)
  }
}