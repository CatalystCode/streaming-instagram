package com.microsoft.catalystcode.fortis.spark.streaming.instagram

import java.util.concurrent.TimeUnit

import com.microsoft.catalystcode.fortis.spark.streaming.PollingSchedule
import com.microsoft.catalystcode.fortis.spark.streaming.instagram.client.{InstagramLocationClient,InstagramTagClient}
import com.microsoft.catalystcode.fortis.spark.streaming.instagram.dto.InstagramItem
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream

object InstagramUtils {
  def createLocationStream(
    ssc: StreamingContext,
    auth: InstagramAuth,
    latitude: Double,
    longitude: Double,
    distance: Double = 1000,
    pollingSchedule: PollingSchedule = PollingSchedule(30, TimeUnit.SECONDS),
    pollingWorkers: Int = 1,
    storageLevel: StorageLevel = StorageLevel.MEMORY_ONLY
  ): ReceiverInputDStream[InstagramItem] = {
    new InstagramInputDStream(
      ssc = ssc,
      client = new InstagramLocationClient(
        latitude = latitude,
        longitude = longitude,
        distance = distance,
        auth = auth),
      pollingSchedule = pollingSchedule,
      pollingWorkers = pollingWorkers,
      storageLevel = storageLevel)
  }

  def createTagStream(
    ssc: StreamingContext,
    auth: InstagramAuth,
    tag: String,
    pollingSchedule: PollingSchedule = PollingSchedule(30, TimeUnit.SECONDS),
    pollingWorkers: Int = 1,
    storageLevel: StorageLevel = StorageLevel.MEMORY_ONLY
  ): ReceiverInputDStream[InstagramItem] = {
    new InstagramInputDStream(
      ssc = ssc,
      client = new InstagramTagClient(
        tag = tag,
        auth = auth),
      pollingSchedule = pollingSchedule,
      pollingWorkers = pollingWorkers,
      storageLevel = storageLevel)
  }
}
