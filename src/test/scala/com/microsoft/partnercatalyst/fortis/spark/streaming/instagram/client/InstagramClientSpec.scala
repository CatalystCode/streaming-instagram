package com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.client

import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.InstagramAuth
import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.dto._
import org.scalatest.FlatSpec

class TestInstagramClient(response: String) extends InstagramClient(InstagramAuth("token")) {
  override protected def fetchInstagramResponse(): String = response
}

class InstagramClientSpec extends FlatSpec {
  "The instagram client" should "produce domain objects from the json api response" in {
    val response = new TestInstagramClient("""
        |{
        |    "data": [{
        |        "type": "image",
        |        "users_in_photo": [],
        |        "filter": "Earlybird",
        |        "tags": ["snow"],
        |        "comments": {
        |            "count": 3
        |        },
        |        "caption": {
        |            "created_time": "1296703540",
        |            "text": "#Snow",
        |            "from": {
        |                "username": "emohatch",
        |                "id": "1242695"
        |            },
        |            "id": "26589964"
        |        },
        |        "likes": {
        |            "count": 1
        |        },
        |        "link": "http://instagr.am/p/BWl6P/",
        |        "user": {
        |            "username": "emohatch",
        |            "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_1242695_75sq_1293915800.jpg",
        |            "id": "1242695",
        |            "full_name": "Dave"
        |        },
        |        "created_time": "1296703536",
        |        "images": {
        |            "low_resolution": {
        |                "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_6.jpg",
        |                "width": 306,
        |                "height": 306
        |            },
        |            "thumbnail": {
        |                "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_5.jpg",
        |                "width": 150,
        |                "height": 150
        |            },
        |            "standard_resolution": {
        |                "url": "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_7.jpg",
        |                "width": 612,
        |                "height": 612
        |            }
        |        },
        |        "id": "22699663",
        |        "location": null
        |      }
        |    ]
        |}
      """.stripMargin).loadNewInstagrams().toList

    assert(response.length === 1)

    assert(response.head === InstagramItem(
      `type` = "image",
      filter = Some("Earlybird"),
      tags = List("snow"),
      comments = InstagramComments(
        count = 3),
      caption = InstagramCaption(
        created_time = "1296703540",
        text = "#Snow",
        from = InstagramUser(
          username = "emohatch",
          id = "1242695"),
        id = "26589964"),
      likes = InstagramLikes(
        count = 1),
      link = "http://instagr.am/p/BWl6P/",
      user = InstagramUser(
        username = "emohatch",
        profile_picture = Some("http://distillery.s3.amazonaws.com/profiles/profile_1242695_75sq_1293915800.jpg"),
        full_name = Some("Dave"),
        id = "1242695"),
      created_time = "1296703536",
      images = InstagramImages(
        low_resolution = InstagramImage(
          url = "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_6.jpg",
          width = 306,
          height = 306),
        thumbnail = InstagramImage(
          url = "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_5.jpg",
          width = 150,
          height = 150),
        standard_resolution = InstagramImage(
          url = "http://distillery.s3.amazonaws.com/media/2011/02/02/f9443f3443484c40b4792fa7c76214d5_7.jpg",
          width = 612,
          height = 612)),
      id = "22699663"))
  }
}
