package com.microsoft.partnercatalyst.fortis.spark.sources.instagram

import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.client.InstagramClient
import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.dto.InstagramItem
import com.microsoft.partnercatalyst.fortis.spark.sources.{PollingReceiver, PollingSchedule}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver

private class InstagramReceiver(
  client: InstagramClient,
  pollingSchedule: PollingSchedule,
  storageLevel: StorageLevel,
  pollingWorkers: Int
) extends PollingReceiver[InstagramItem](pollingSchedule, pollingWorkers, storageLevel) {

  @volatile private var lastIngestedEpoch = Long.MinValue

  override protected def poll(): Unit = {
    client
      .loadNewInstagrams()
      .foreach(x => {
        store(x)
        markStored(x)
      })
  }

  private def markStored(item: InstagramItem): Unit = {
    val itemCreatedAt = item.created_time.toLong
    if (itemCreatedAt > lastIngestedEpoch) {
      lastIngestedEpoch = itemCreatedAt
    }
  }
}

class InstagramInputDStream(
  ssc: StreamingContext,
  client: InstagramClient,
  pollingSchedule: PollingSchedule,
  pollingWorkers: Int,
  storageLevel: StorageLevel
) extends ReceiverInputDStream[InstagramItem](ssc) {

  override def getReceiver(): Receiver[InstagramItem] = {
    new InstagramReceiver(client, pollingSchedule, storageLevel, pollingWorkers)
  }
}
