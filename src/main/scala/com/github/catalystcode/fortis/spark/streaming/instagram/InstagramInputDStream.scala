package com.github.catalystcode.fortis.spark.streaming.instagram

import com.github.catalystcode.fortis.spark.streaming.instagram.client.InstagramClient
import com.github.catalystcode.fortis.spark.streaming.instagram.dto.InstagramItem
import com.github.catalystcode.fortis.spark.streaming.{PollingReceiver, PollingSchedule}
import org.apache.log4j.LogManager
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

  @transient private lazy val log = LogManager.getLogger("libinstagram")
  @volatile private var lastIngestedEpoch = Long.MinValue

  override protected def poll(): Unit = {
    client
      .loadNewInstagrams()
      .filter(x => {
        val isNew = x.created_time.toLong > lastIngestedEpoch
        log.info(s"Got instagram ${x.link}, isNew = $isNew")
        isNew
      })
      .foreach(x => {
        log.info(s"Storing instagram ${x.link}")
        store(x)
        markStored(x)
      })
  }

  private def markStored(item: InstagramItem): Unit = {
    val itemCreatedAt = item.created_time.toLong
    if (itemCreatedAt > lastIngestedEpoch) {
      lastIngestedEpoch = itemCreatedAt
      log.info(s"Updating last ingested epoch to $itemCreatedAt")
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
    logDebug("Creating instagram receiver")
    new InstagramReceiver(client, pollingSchedule, storageLevel, pollingWorkers)
  }
}
