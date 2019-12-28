package com.doumi.biz.byscala

import com.doumi.biz.util.ScalaUtil
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object SparkReadJson {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName(getClass.getSimpleName)
      .getOrCreate()

    val fileName = ScalaUtil.getCurrentDir + "/data/1.json"
    println(fileName)

    val schema = StructType(
      Array[StructField](
        StructField("id", IntegerType, true)
        , StructField("name", StringType, true)
        , StructField("age", IntegerType, true)
        , StructField("data", StringType, true)
      )
    )
    spark
        .read
        .format("json")
        .load(fileName)
        .show()

    spark.stop

  }
}
