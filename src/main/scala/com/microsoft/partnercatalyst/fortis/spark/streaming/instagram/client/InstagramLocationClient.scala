package com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.client

import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.InstagramAuth

import scala.io.Source

@SerialVersionUID(100L)
class InstagramLocationClient(
  latitude: Double,
  longitude: Double,
  distance: Double,
  auth: InstagramAuth
) extends InstagramClient(auth) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = {
    val fetch = url.getOrElse(s"https://${auth.apiHost}/v1/media/search?lat=$latitude&lng=$longitude&distance=$distance&access_token=${auth.accessToken}")
    Source.fromURL(fetch).mkString
  }
}
