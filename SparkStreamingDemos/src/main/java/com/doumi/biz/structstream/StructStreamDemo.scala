package com.doumi.biz.structstream

import com.doumi.biz.listener.BizStreamQueryListener
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object StructStreamDemo {
  Logger.getLogger("org").setLevel(Level.WARN)

  val streamTag = getClass.getSimpleName

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test")
      .option("maxOffsetsPerTrigger", 100 * 10000)
      .load()
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)", "timestamp", "topic", "partition", "offset")
      .as[(String, String, Long, String, Int, Long)]

    spark.streams.addListener(new BizStreamQueryListener(streamTag))

    df
      .writeStream
      .format("console")
      .start()

    spark.streams.awaitAnyTermination()

  }
}
