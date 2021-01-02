
package yycore

import chisel3._
import chisel3.iotesters.PeekPokeTester
import common.Constants._
import Exe.BrCond


class BrCondTest(c: BrCond) extends PeekPokeTester(c){
  def eq (a:Int, b:Int): Boolean = a == b
  def neq(a:Int, b:Int): Boolean = a != b
  def lt (a:Int, b:Int): Boolean = a < b
  def ge (a:Int, b:Int): Boolean = a >= b
  def ltu(a:Long, b:Long): Boolean = a < b   // 用long代替无符号数
  def geu(a:Long, b:Long): Boolean = a >= b
//  def ltu1(a:UInt, b:UInt): Bool = a < b


  private val br = c

//  // test lt
//  for(i <- 1 to 10){
//    val A = rnd.nextInt()
//    val B = rnd.nextInt()
//    println("A = " + A)
//    println("B = " + B)
//    //input
//    poke(br.io.rs1, A)
//    poke(br.io.rs2, B)
//    poke(br.io.br_type, BR_LT)
//    // cal
//    val out = lt(A, B)
//    // out
//    expect(br.io.taken, out)
//    step(1)
//  }
//
//
//  // test ltu
//  for(i <- 1 to 10){
////    val A = rnd.nextInt()
////    val B = rnd.nextInt()
//    val A :Short = rnd.nextInt().toShort
//    val B :Short = rnd.nextInt().toShort
//    val UA :Int = A & 0x0FFFF;    // 改成无符号数
//    val UB :Int = B & 0x0FFFF;
//    println("A = " + A + " UA " + UA)
//    println("B = " + B + " UB " + UB)
//    //input
//    poke(br.io.rs1, UA)
//    poke(br.io.rs2, UB)
//    poke(br.io.br_type, BR_LTU)
//    // cal
//    val out = ltu(UA, UB)
//    // out
//    expect(br.io.taken, out)
//    step(1)
//  }

  // test ltu2
  for(i <- 1 to 10){
    //    val A = rnd.nextInt()
    //    val B = rnd.nextInt()
    val A = rnd.nextInt()
    val B = rnd.nextInt()
    val UA = Integer.toUnsignedLong(A)
    val UB = Integer.toUnsignedLong(B)
//    val UA1 = A.asUInt()
//    val UB1 = B.asUInt()
    println("A = " + A + " UA " + UA)
    println("B = " + B + " UB " + UB)
    //input
    poke(br.io.rs1, UA)
    poke(br.io.rs2, UB)
    poke(br.io.br_type, BR_LTU)
    // cal
    val out = ltu(UA, UB)
    // out
    expect(br.io.taken, out)
    step(1)
  }




}

object BrCondTestGen extends App{
  iotesters.Driver.execute(args, () => new BrCond) {
    c => new BrCondTest(c)
  }
}