package com.examples

import java.io.FileNotFoundException
import scala.collection.mutable.ListBuffer
import com.examples.FizzbuzzParser._
import org.scalatest.flatspec.AnyFlatSpec

class FizzbuzzParserSuite extends AnyFlatSpec {
  it should "load the file" in {
    assert(read("src/test/resources/test.log").size > 0)
    assertThrows[FileNotFoundException] {
      read("x/y/z")
    }
  }

  it should "parser the in put log" in {
    val stream = parser(read("src/test/resources/test.log"))

    assert(stream.head == Some(("standard", "256", "19", "263920", "186677")))
    assert(stream.size == 18)
  }

  it should "make a statistic of the number of viewmodes" in {
    val res = statistic(parser(read("/Users/apple/Fizzbuzz/src/test/resources/test.log")), true)

    assert(res.size == 9)
    assert(res == List(StatisticItem("standard", 4, ListBuffer("19", "12", "14")), StatisticItem("traffic", 1, ListBuffer("14")),
      StatisticItem("traffic_hd", 2, ListBuffer("12")), StatisticItem("standard_hd", 1, ListBuffer("14")),
      StatisticItem("traffic", 2, ListBuffer("14", "17")), StatisticItem("standard", 2, ListBuffer("19", "17")),
      StatisticItem("public_transport_hd", 1, ListBuffer("15")), StatisticItem("standard", 4, ListBuffer("18", "19", "14")),
      StatisticItem("standard_hd", 1, ListBuffer("18"))))
  }

}
