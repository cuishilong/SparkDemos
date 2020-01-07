package com.doumi.biz.ml.model

import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object MLLinearRegressionModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(getClass.getSimpleName.replaceAll("\\$", ""))
      .master("local[*]")
      .getOrCreate()

    val dataInit = Array(
      (3,Vectors.dense(2,1))
      ,(4,Vectors.dense(3,1))
      ,(5,Vectors.dense(4,1))
      ,(7,Vectors.dense(6,1))
    )
    val dataDf = spark.createDataFrame(dataInit).toDF("label","features")

    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(1.0)
      .setElasticNetParam(0.0)

    val Array(trainData, testData) = dataDf.randomSplit(Array(0.9, 0.1))

    val model = lr.fit(dataDf)

    val predictionData = model.transform(dataDf)

    predictionData.orderBy("prediction").show(100)

    val summary = model.summary

    println(summary.rootMeanSquaredError)
    println(summary.r2)

    spark.stop()
  }
}
