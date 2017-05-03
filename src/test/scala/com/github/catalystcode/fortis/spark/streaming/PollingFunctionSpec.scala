package com.github.catalystcode.fortis.spark.streaming

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.FlatSpec

class PollingFunctionSpec extends FlatSpec {
  "The polling function" should "execute a callback on a schedule" in {
    val executionCount = new AtomicInteger()
    val poller = new PollingFunction(executionCount.incrementAndGet, PollingSchedule(7, TimeUnit.MILLISECONDS, 8), 1)

    poller.start()
    Thread.sleep(11)
    poller.stop()

    assert(executionCount.get == 1)
  }
}
