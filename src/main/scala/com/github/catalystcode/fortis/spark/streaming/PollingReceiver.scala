package com.github.catalystcode.fortis.spark.streaming

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import org.apache.log4j.LogManager
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

case class PollingSchedule(interval: Long, unit: TimeUnit, initialDelay: Long = 1)

class PollingFunction(
  callback: () => Any,
  pollingSchedule: PollingSchedule,
  pollingWorkers: Int
) {

  @transient private lazy val log = LogManager.getLogger("libinstagram")
  private var threadPool: ScheduledThreadPoolExecutor = _

  def start(): Unit = {
    log.debug("Creating polling threadpool")
    threadPool = new ScheduledThreadPoolExecutor(pollingWorkers)

    log.debug("Setting up polling thread")
    val pollingThread = new Thread("Polling thread") {
      override def run(): Unit = {
        log.debug("Executing polling function")
        callback()
      }
    }

    log.debug("Scheduling polling thread")
    threadPool.scheduleAtFixedRate(
      pollingThread, pollingSchedule.initialDelay,
      pollingSchedule.interval, pollingSchedule.unit)
  }

  def stop(): Unit = {
    if (threadPool != null) {
      log.debug("Shutting down polling threadpool")
      threadPool.shutdown()
    }
  }
}

abstract class PollingReceiver[T](
 pollingSchedule: PollingSchedule,
 pollingWorkers: Int,
 storageLevel: StorageLevel
) extends Receiver[T](storageLevel) {

  @transient private lazy val log = LogManager.getLogger("libinstagram")
  private var pollingFunction: PollingFunction = _

  def onStart(): Unit = {
    log.debug("Starting polling receiver")
    pollingFunction = new PollingFunction(poll, pollingSchedule, pollingWorkers)
  }

  def onStop(): Unit = {
    if (pollingFunction != null) {
      log.debug("Shutting down polling receiver")
      pollingFunction.stop()
    }
  }

  protected def poll(): Unit
}
