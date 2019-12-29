package com.doumi.biz.stream

import com.doumi.biz.listener.BizStreamListener
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}


object StreamDemo {
  Logger.getLogger("org").setLevel(Level.WARN)
  val streamTag = getClass.getSimpleName
  val streamMaster = "local[*]"
  val consumerGroup = "test"

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(streamTag)
      .setMaster(streamMaster)

    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.addStreamingListener(new BizStreamListener(streamTag))

    val topics = Array[String]("test")
    val kafkaParams = Map[String, Object](
      ("bootstrap.servers", "localhost:9092")
      , ("key.deserializer", classOf[StringDeserializer])
      , ("value.deserializer", classOf[StringDeserializer])
      , ("group.id", consumerGroup)
      , ("auto.offset.reset", "latest")
      , ("enable.auto.commit", "false")
    )

    val stream = KafkaUtils
      .createDirectStream[String, String](
        ssc
        , PreferConsistent
        , Subscribe[String, String](topics, kafkaParams)
      )


    stream
      .map(record => (record.key(), record.value(), record.timestamp(), record.partition()))
      .print()

    ssc.start()
    ssc.awaitTermination()
  }
}
