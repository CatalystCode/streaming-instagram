package com.microsoft.catalystcode.fortis.spark.streaming

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

case class PollingSchedule(interval: Long, unit: TimeUnit, initialDelay: Long = 1)

class PollingFunction(
  callback: () => Any,
  pollingSchedule: PollingSchedule,
  pollingWorkers: Int
) {

  private var threadPool: ScheduledThreadPoolExecutor = _

  def start(): Unit = {
    threadPool = new ScheduledThreadPoolExecutor(pollingWorkers)

    val pollingThread = new Thread("Polling thread") {
      override def run(): Unit = {
        callback()
      }
    }

    threadPool.scheduleAtFixedRate(
      pollingThread, pollingSchedule.initialDelay,
      pollingSchedule.interval, pollingSchedule.unit)
  }

  def stop(): Unit = {
    if (threadPool != null) {
      threadPool.shutdown()
    }
  }
}

abstract class PollingReceiver[T](
 pollingSchedule: PollingSchedule,
 pollingWorkers: Int,
 storageLevel: StorageLevel
) extends Receiver[T](storageLevel) {

  private var pollingFunction: PollingFunction = _

  def onStart(): Unit = {
    pollingFunction = new PollingFunction(poll, pollingSchedule, pollingWorkers)
  }

  def onStop(): Unit = {
    if (pollingFunction != null) {
      pollingFunction.stop()
    }
  }

  protected def poll(): Unit
}
