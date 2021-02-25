package com.examples

import com.examples.FizzbuzzParser.print

object FizzbuzzMain extends Main {
  override def run(argsMap: Map[String, String]): Int = {
    print(argsMap("file"), argsMap.getOrElse("calculZoom", "false").toBoolean)
    1
  }
}
