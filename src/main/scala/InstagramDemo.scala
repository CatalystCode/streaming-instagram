import com.github.catalystcode.fortis.spark.streaming.instagram.client.{InstagramLocationClient, InstagramTagClient}
import com.github.catalystcode.fortis.spark.streaming.instagram.{InstagramAuth, InstagramUtils}
import org.apache.log4j.{BasicConfigurator, Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object InstagramDemo {
  def main(args: Array[String]) {
    // configure location for which to ingest images
    // also configure tag for which to ingest images
    val mode = args.headOption.getOrElse("")
    val latitude = 49.25
    val longitude = -123.1
    val tag = "rose"

    // configure interaction with instagram api
    val auth = InstagramAuth(System.getenv("INSTAGRAM_AUTH_TOKEN"))

    // configure logging
    BasicConfigurator.configure()
    Logger.getRootLogger.setLevel(Level.ERROR)
    Logger.getLogger("libinstagram").setLevel(Level.DEBUG)

    if (mode.contains("standalone")) {
      println(new InstagramLocationClient(latitude = latitude, longitude = longitude, distance = 1000, auth = auth).loadNewInstagrams().toList)
      println(new InstagramTagClient(tag = tag, auth = auth).loadNewInstagrams().toList)
    }

    if (mode.contains("spark")) {
      // set up the spark context and streams
      val conf = new SparkConf().setAppName("Instagram Spark Streaming Demo Application")
      val sc = new SparkContext(conf)
      val ssc = new StreamingContext(sc, Seconds(1))

      InstagramUtils.createLocationStream(ssc, auth, latitude, longitude)
        .union(InstagramUtils.createTagStream(ssc, auth, tag))
        .print()

      // run forever
      ssc.start()
      ssc.awaitTermination()
    }
  }
}
