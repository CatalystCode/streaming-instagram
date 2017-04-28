A library for reading social data from [Instagram](http://instagram.com) using Spark Streaming.

[![Travis CI status](https://api.travis-ci.org/CatalystCode/streaming-instagram.svg?branch=master)](https://travis-ci.org/CatalystCode/streaming-instagram)

## Usage example ##

Run a demo via:

```sh
# compile scala, run tests, build fat jar
sbt assembly

# run on spark
spark-submit --class Demo --master local[4] target/scala-2.11/streaming-instagram-assembly-0.0.1-alpha.jar
```

Remember to update the Instagram access token in [Demo.scala](https://github.com/CatalystCode/streaming-instagram/blob/master/src/main/scala/Demo.scala)!

## How does it work? ##

Instagram doesn't expose a firehose API so we resort to polling. The InstagramReceiver pings the Instagram API every few
seconds and pushes any new images into Spark Streaming for further processing.

Currently, the following ways to read images are supported:
- by location ([sample data](https://www.instagram.com/explore/locations/213819997/vancouver-british-columbia/))
- by tag ([sample data](https://www.instagram.com/explore/tags/rose/))
