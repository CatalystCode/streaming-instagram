package com.microsoft.partnercatalyst.fortis.spark.sources.instagram.client

import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.InstagramAuth

import scala.io.Source

@SerialVersionUID(100L)
class InstagramLocationClient(
  latitude: Double,
  longitude: Double,
  distance: Double,
  auth: InstagramAuth
) extends InstagramClient(auth) {
  override protected def fetchInstagramResponse(): String = {
    val url = s"https://${auth.apiHost}/v1/media/search?lat=$latitude&lng=$longitude&distance=$distance&access_token=${auth.accessToken}"
    Source.fromURL(url).mkString
  }
}
