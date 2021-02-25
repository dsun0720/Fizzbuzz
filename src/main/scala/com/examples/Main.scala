package com.examples

import scala.util.{Failure, Success, Try}

trait Main {
  def main(args: Array[String]): Unit = {
    val argsMap = args.map(
      arg => arg.split("=")(0) -> arg.split("=")(1)
    ).toMap
    Try {
      run(argsMap)
    } match {
      case Success(_) => println("-------Good Job----------")
      case Failure(ex) => ex.printStackTrace()
    }
  }

  def run(argsMap: Map[String, String]): Int

}
