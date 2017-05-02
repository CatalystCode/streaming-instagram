package com.microsoft.catalystcode.fortis.spark.streaming.instagram.client

import java.io.IOException

import com.microsoft.catalystcode.fortis.spark.streaming.instagram.InstagramAuth
import com.microsoft.catalystcode.fortis.spark.streaming.instagram.dto._
import org.scalatest.FlatSpec

class TestInstagramClient(responses: Map[Option[String], String]) extends InstagramClient(InstagramAuth("token")) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = responses(url)
}

class TestExceptionInstagramClient(exception: Exception) extends InstagramClient(InstagramAuth("token")) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = throw exception
}

class InstagramClientSpec extends FlatSpec {
  private val nextUrl = (maxId: String) => s"https://api.instagram.com/v1/tags/puppy/media/recent?access_token=fb2e77d.47a0479900504cb3ab4a1f626d174d2d&max_id=$maxId"
  private val item = (id: String) => s"""
    |      {
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
    |        "id": "$id",
    |        "location": null
    |      }
    """.stripMargin
  private val itemObj = (id: String) => InstagramItem(
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
      id = id)
  private val responseWithoutPagination = (id: String) => s"""
    |{
    |    "data": [${item(id)}]
    |}
    """.stripMargin
  private val responseWithPagination = (id: String) => s"""
    |{
    |    "pagination": {
    |      "next_url": "${nextUrl(id)}",
    |      "next_max_id": "$id"
    |    },
    |    "data": [${item(id)}]
    |}
    """.stripMargin

  "The instagram client" should "produce domain objects from the json api response" in {
    val id = "123"
    val client = new TestInstagramClient(Map(
      None -> responseWithoutPagination(id)))

    val response = client.loadNewInstagrams().toList

    assert(response.length === 1)
    assert(response.head === itemObj(id))
  }

  it should "dereference pagination" in {
    val (id1, id2, id3) = ("123", "456", "789")
    val client = new TestInstagramClient(Map(
      None -> responseWithPagination(id1),
      Some(nextUrl(id1)) -> responseWithPagination(id2),
      Some(nextUrl(id2)) -> responseWithoutPagination(id3)))

    val response = client.loadNewInstagrams().toList

    assert(response === List(itemObj(id1), itemObj(id2), itemObj(id3)))
  }

  it should "handle exceptions" in {
    val client = new TestExceptionInstagramClient(new IOException())

    val response = client.loadNewInstagrams()

    assert(response === List())
  }
}
