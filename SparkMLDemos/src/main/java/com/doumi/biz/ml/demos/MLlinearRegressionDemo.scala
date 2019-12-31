package com.doumi.biz.ml.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object MLlinearRegressionDemo {
  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMastre = "local[*]"
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMastre)
      .getOrCreate()

    val path = s"${ScalaUtil.getCurrentDir}/data/sample_linear_regression_data.txt"
    val data = spark
      .read
      .format("libsvm")
      .load(path)

    data.show()

    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val model = lr.fit(data)

    println(model.coefficients)

    spark.stop()
  }
}
