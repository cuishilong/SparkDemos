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

    spark.stop()
  }

  def getPredictData(spark: SparkSession): DataFrame = {
    val vv1 = Vectors.dense(1, 1)
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
      (1, v1)
      , (1, v2)
      , (2, v3)
      , (2, v4)
      , (3, v5)
      , (3, v6)
      , (4, v7)
      , (4, v8)
    )
    spark.createDataFrame(dataInit).toDF("label", "features")
  }
}
