package com.doumi.biz.mllib.demos

import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object MLCorrelationDemo {

  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMaster = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMaster)
      .getOrCreate()
    import spark.implicits._
    val sc = spark.sparkContext

    val arr = Array[Double](1, 2)
    val denseVector = Vectors.dense(arr)
    val denseVector2 = Vectors.dense(arr.map(_ + 10))

    val sparseVector = Vectors.sparse(9, Seq[(Int, Double)]((0, 10), (1, 20)))
    val sparseVector2 = Vectors.sparse(9, Seq[(Int, Double)]((0, 20), (1, 40)))

    val data: RDD[Vector] = sc.parallelize(Array[Vector](denseVector, denseVector2))

    val correlation: Matrix = Statistics.corr(data, "pearson")

    println(correlation.toString())

    spark.stop()
  }
}
