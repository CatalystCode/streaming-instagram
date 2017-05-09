import com.github.catalystcode.fortis.spark.streaming.instagram.InstagramAuth
import com.github.catalystcode.fortis.spark.streaming.instagram.client.{InstagramLocationClient, InstagramTagClient, InstagramUserClient}

class InstagramDemoStandalone(latitude: Double, longitude: Double, tag: String, userId: String, auth: InstagramAuth) {
  def run(): Unit = {
    println(new InstagramLocationClient(latitude = latitude, longitude = longitude, distance = 1000, auth = auth).loadNewInstagrams().toList)
    println(new InstagramTagClient(tag = tag, auth = auth).loadNewInstagrams().toList)
    println(new InstagramUserClient(userId = userId, auth = auth).loadNewInstagrams().toList)
  }
}
