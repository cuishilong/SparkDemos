package com.doumi.biz.ml.model

import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, SparkSession}

object MLMultinomialLogisticRegressionModel {
  val tag = getClass.getSimpleName.replaceAll("\\$", "")
  val master = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(tag)
      .master(master)
      .getOrCreate()

    val data = featuresProject(spark)

    val model = generateModel(data)

    val predictData = getPredictData(spark)

    model.transform(predictData).show()

    println(model.summary.accuracy)

    spark.stop()
  }

  def getPredictData(spark: SparkSession): DataFrame = {
    val vv1 = Vectors.dense(10, 10)
    val vv2 = Vectors.dense(-10.0, -20.0)
    val dataInit = Array(vv1, vv2)
    spark.createDataFrame(dataInit.map(Tuple1.apply)).toDF("features")
  }

  def generateModel(data: DataFrame): LogisticRegressionModel = {
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)
//      .setFamily("multinomial")
    lr.fit(data)
  }

  def featuresProject(spark: SparkSession): DataFrame = {
    val v1 = Vectors.dense(1.0, 2.0)
    val v2 = Vectors.dense(10.0, 20.0)
    val v3 = Vectors.dense(1.0, -2.0)
    val v4 = Vectors.dense(10.0, -20.0)
    val v5 = Vectors.dense(-1.0, 2.0)
    val v6 = Vectors.dense(-10.0, 20.0)
    val v7 = Vectors.dense(-1.0, -2.0)
    val v8 = Vectors.dense(-10.0, -20.0)

    val dataInit = Array(
      (0, v1)
      , (0, v2)
      , (1, v3)
      , (1, v4)
      , (1, v5)
      , (1, v6)
      , (0, v7)
      , (0, v8)
    )
    spark.createDataFrame(dataInit).toDF("label", "features")
  }
}
