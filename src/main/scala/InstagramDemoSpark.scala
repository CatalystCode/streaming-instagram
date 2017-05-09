import com.github.catalystcode.fortis.spark.streaming.instagram.{InstagramAuth, InstagramUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

class InstagramDemoSpark(latitude: Double, longitude: Double, tag: String, userId: String, auth: InstagramAuth) {
  def run(): Unit = {
    // set up the spark context and streams
    val conf = new SparkConf().setAppName("Instagram Spark Streaming Demo Application")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(1))

    InstagramUtils.createLocationStream(ssc, auth, latitude, longitude)
      .union(InstagramUtils.createTagStream(ssc, auth, tag))
      .union(InstagramUtils.createUserStream(ssc, auth, userId))
      .print()

    // run forever
    ssc.start()
    ssc.awaitTermination()
  }

}
