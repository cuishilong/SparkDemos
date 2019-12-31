package com.doumi.biz.ml.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object MLlinearRegressionDemo2 {
  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMastre = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMastre)
      .getOrCreate()

    val dataInit = Array(
      (3, Vectors.dense(1, 1, 1))
      , (6, Vectors.dense(3, 2, 1))
      , (90, Vectors.dense(10, 80, 0))
      , (55, Vectors.dense(34, 15, 6))
    )
    val df = spark.createDataFrame(dataInit).toDF("label", "features")

    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val model = lr.fit(df)

    val predictInit = Array(
      Vectors.dense(1, 1, 1)
      , Vectors.dense(6, 9, 5)
    )

    val predictData = spark.createDataFrame(predictInit.map(Tuple1.apply)).toDF("features")

    model.transform(predictData).show()

    println(model.coefficients)

    spark.stop()
  }
}
