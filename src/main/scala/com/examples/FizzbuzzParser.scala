package com.examples

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try

case class StatisticItem(key: String, var sum: Int, buffer: ListBuffer[String])

trait FizzbuzzParserOps {
  self: FizzbuzzParser =>

  //data loading
  def read(file: String): Iterator[String] = {
    Source.fromFile(file).getLines()
  }

  //parser plans from urls and filter the invalid urls
  def parser(logs: Iterator[String]): Stream[Option[(String, String, String, String, String)]] = {
    logs.toStream.map(log => {
      Try {
        val splits = log.split(DEFAULT_LOG_DELIMITER)
        if (splits.size > 1) {
          val planOrigin = splits(1).split(DEFAULT_PLAN_PROP_DELIMITER)
          val (prefix, plan) = planOrigin.splitAt(DEFAULT_PLAN_POSITION)
          //check if the item is a plan
          if (planOrigin.length == DEFAULT_PLAN_CHAIN_LEN && prefix.mkString("/") == DEFAULT_PLAN_PREFIX)
            Some((plan(0), plan(1), plan(2), plan(3), plan(4)))
          else
            None
        } else None
      } recover {
        case exception: Exception =>
          sys.error(f"error line : ${log}")
          None
      }
    }.get).filter(_.isDefined)
  }

  //output the number of identical kw(viewmodes) that follow each other in the lines with the number of occurrences.
  def statistic(plans: Stream[Option[(String, String, String, String, String)]], calculZoom: Boolean): List[StatisticItem] = {
    plans.foldLeft(List.empty[StatisticItem]) {
      case (list, Some((k, _, zoom, _, _))) =>
        if (list.isEmpty) List(StatisticItem(k, 1, if (calculZoom) ListBuffer(zoom) else ListBuffer.empty[String]))
        else {
          if (k == list.head.key) {
            list.head.sum = list.head.sum + 1
            if (calculZoom && !list.head.buffer.contains(zoom)) list.head.buffer += zoom
            list
          }
          else
            StatisticItem(k, 1, if (calculZoom) ListBuffer(zoom) else ListBuffer.empty[String]) :: list
        }
    }.reverse
  }


}

trait FizzbuzzParser extends FizzbuzzParserOps {
  val DEFAULT_LOG_DELIMITER = "\\t"
  val DEFAULT_PLAN_PROP_DELIMITER = "/"
  val DEFAULT_PLAN_CHAIN_LEN = 9
  val DEFAULT_PLAN_PREFIX = "/map/1.0/slab"
  val DEFAULT_PLAN_POSITION = 4
}

object FizzbuzzParser extends FizzbuzzParser {
  def print(file: String, calculZoom: Boolean = true): Unit = {
    statistic(parser(read(file)), calculZoom).foreach(line => println(f"${line.key}\t${line.sum}\t${line.buffer.mkString(",")}"))
  }
}
