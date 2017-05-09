import com.github.catalystcode.fortis.spark.streaming.instagram.InstagramAuth
import org.apache.log4j.{BasicConfigurator, Level, Logger}

object InstagramDemo {
  def main(args: Array[String]) {
    val mode = args.headOption.getOrElse("")

    // configure location, tag, user, etc. for which to ingest images
    val latitude = 49.25
    val longitude = -123.1
    val tag = "rose"
    val userId = "700400804"

    // configure interaction with instagram api
    val auth = InstagramAuth(System.getenv("INSTAGRAM_AUTH_TOKEN"))

    // configure logging
    BasicConfigurator.configure()
    Logger.getRootLogger.setLevel(Level.ERROR)
    Logger.getLogger("libinstagram").setLevel(Level.DEBUG)

    if (mode.contains("standalone")) new InstagramDemoStandalone(latitude, longitude, tag, userId, auth).run()
    if (mode.contains("spark")) new InstagramDemoSpark(latitude, longitude, tag, userId, auth).run()
  }
}
