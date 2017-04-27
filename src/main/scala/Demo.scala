import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.{InstagramAuth, InstagramUtils}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Demo {
  def main(args: Array[String]) {
    // configure location for which to ingest images
    // also configure tag for which to ingest images
    val latitude = 49.25
    val longitude = -123.1
    val tag = "rose"

    // configure interaction with instagram api
    val auth = InstagramAuth("INSERT_INSTAGRAM_AUTH_TOKEN_HERE")

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
