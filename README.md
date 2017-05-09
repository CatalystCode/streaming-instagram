A library for reading social data from [Instagram](http://instagram.com) using Spark Streaming.

[![Travis CI status](https://api.travis-ci.org/CatalystCode/streaming-instagram.svg?branch=master)](https://travis-ci.org/CatalystCode/streaming-instagram)

## Usage example ##

Run a demo via:

```sh
# set up all the requisite environment variables
export INSTAGRAM_AUTH_TOKEN="..."

# compile scala, run tests, build fat jar
sbt assembly

# run locally
java -cp target/scala-2.11/streaming-instagram-assembly-0.0.5.jar InstagramDemo standalone

# run on spark
spark-submit --class InstagramDemo --master local[2] target/scala-2.11/streaming-instagram-assembly-0.0.5.jar spark
```

## How does it work? ##

Instagram doesn't expose a firehose API so we resort to polling. The InstagramReceiver pings the Instagram API every few
seconds and pushes any new images into Spark Streaming for further processing.

Currently, the following ways to read images are supported:
- by location ([sample data](https://www.instagram.com/explore/locations/213819997/vancouver-british-columbia/))
- by tag ([sample data](https://www.instagram.com/explore/tags/rose/))
- by user ([sample data](https://www.instagram.com/viawesome/))

## Release process ##

1. Configure your credentials via the `SONATYPE_USER` and `SONATYPE_PASSWORD` environment variables.
2. Update `version.sbt`
3. Run `sbt sonatypeOpen "enter staging description here"`
4. Run `sbt publishSigned`
5. Run `sbt sonatypeRelease`
