package com.doumi.biz.ml.demos

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.linalg.{Matrix, Vectors => MLVectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.{Row, SparkSession}

object MLCorrelationiDemo {
  Logger.getLogger("org").setLevel(Level.WARN)
  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMaster = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMaster)
      .getOrCreate()
    import spark.implicits._

    val data = Seq(
      //      MLVector.sparse(5, Seq((0, 1.0), (3, -2.0))),
      MLVectors.dense(4.0, 5.0, 0.0, 3.0),
      MLVectors.dense(6.0, 7.0, 0.0, 8.0)
      //      MLVector.sparse(5, Seq((0, 9.0), (3, 1.0)))
    )

    val df = data.map(Tuple1.apply).toDF("features")
    val Row(coeff1: Matrix) = Correlation.corr(df, "features").head
    println(s"Pearson correlation matrix:\n $coeff1")

    val Row(coeff2: Matrix) = Correlation.corr(df, "features", "spearman").head
    println(s"Spearman correlation matrix:\n $coeff2")

    spark.stop()

  }
}
