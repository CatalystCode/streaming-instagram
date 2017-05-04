package com.github.catalystcode.fortis.spark.streaming.instagram

import com.github.catalystcode.fortis.spark.streaming.instagram.client.InstagramClient
import com.github.catalystcode.fortis.spark.streaming.instagram.dto.InstagramItem
import com.github.catalystcode.fortis.spark.streaming.{PollingReceiver, PollingSchedule}
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
      .filter(x => x.created_time.toLong > lastIngestedEpoch)
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
