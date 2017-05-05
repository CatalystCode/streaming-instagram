package com.github.catalystcode.fortis.spark.streaming

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import org.apache.log4j.LogManager
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

case class PollingSchedule(interval: Long, unit: TimeUnit, initialDelay: Long = 1)

abstract class PollingReceiver[T](
 pollingSchedule: PollingSchedule,
 pollingWorkers: Int,
 storageLevel: StorageLevel
) extends Receiver[T](storageLevel) {

  @transient private lazy val log = LogManager.getLogger("libinstagram")
  private var threadPool: ScheduledThreadPoolExecutor = _

  def onStart(): Unit = {
    log.debug("Creating polling threadpool")
    threadPool = new ScheduledThreadPoolExecutor(pollingWorkers)

    log.debug("Setting up polling thread")
    val pollingThread = new Thread("Polling thread") {
      override def run(): Unit = {
        log.debug("Executing polling function")
        poll()
      }
    }

    log.debug("Scheduling polling thread")
    threadPool.scheduleAtFixedRate(
      pollingThread, pollingSchedule.initialDelay,
      pollingSchedule.interval, pollingSchedule.unit)
  }

  def onStop(): Unit = {
    if (threadPool != null) {
      log.debug("Shutting down polling threadpool")
      threadPool.shutdown()
    }
  }

  protected def poll(): Unit
}
