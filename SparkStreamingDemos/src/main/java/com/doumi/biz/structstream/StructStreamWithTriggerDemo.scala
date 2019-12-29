package com.doumi.biz.structstream

import com.alibaba.fastjson.JSON
import com.doumi.biz.listener.BizStreamQueryListener
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

object StructStreamWithTriggerDemo {
  Logger.getLogger("org").setLevel(Level.WARN)
  val streamTag = getClass.getSimpleName
  val streamMaster = "local[*]"

  case class Bean(id: Int, name: String, timestamp: Long, topic: String, partition: Int, offset: Long)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMaster)
      .getOrCreate()
    import spark.implicits._

    spark.streams.addListener(new BizStreamQueryListener(streamTag))

    val streamDf = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test")
      .option("maxOffsetsPerTrigger", 100 * 10000)
      .load()
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)", "timestamp", "topic", "partition", "offset")
      .map(row => {
        val json = JSON.parseObject(row.getAs[String]("value"))
        val id = row.getAs[String]("key").toInt
        val name = json.getString("name")
        val topic = row.getAs[String]("topic")
        val timestamp = row.getAs[java.sql.Timestamp]("timestamp")
        val partition = row.getAs[Int]("partition")
        val offset = row.getAs[Long]("offset")
        Bean(id, name, timestamp.getTime, topic, partition, offset)
      })

    streamDf
      .writeStream
      .trigger(Trigger.ProcessingTime(1000))
      .format("console")
      .start()

    spark.streams.awaitAnyTermination()
    spark.stop()
  }
}
