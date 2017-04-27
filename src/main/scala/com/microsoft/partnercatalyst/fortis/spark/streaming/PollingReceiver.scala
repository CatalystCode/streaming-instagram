package com.microsoft.partnercatalyst.fortis.spark.streaming

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

case class PollingSchedule(interval: Long, unit: TimeUnit, initialDelay: Long = 1)

abstract class PollingReceiver[T](
 pollingSchedule: PollingSchedule,
 pollingWorkers: Int,
 storageLevel: StorageLevel
) extends Receiver[T](storageLevel) {

  private var threadPool: ScheduledThreadPoolExecutor = _

  def onStart(): Unit = {
    threadPool = new ScheduledThreadPoolExecutor(pollingWorkers)

    val pollingThread = new Thread("Polling thread") {
      override def run(): Unit = {
        poll()
      }
    }

    threadPool.scheduleAtFixedRate(
      pollingThread, pollingSchedule.initialDelay,
      pollingSchedule.interval, pollingSchedule.unit)
  }

  def onStop(): Unit = {
    if (threadPool != null) {
      threadPool.shutdown()
    }
  }

  protected def poll(): Unit
}
