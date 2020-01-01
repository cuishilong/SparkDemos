package com.doumi.biz.ml.features

import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, SparkSession}

object LogisticRegressionFeatures {
  val tag = getClass.getSimpleName.replaceAll("\\$", "")
  val master = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(tag)
      .master(master)
      .getOrCreate()

    val dataTrain = featuresProject(spark)

    val model = generateModel(dataTrain)

    val predictData = getPreparedPredictData(spark)

    val prediction = doPrediction(predictData, model)

    prediction.show()

    spark.stop()
  }

  def doPrediction(data: DataFrame, model: LogisticRegressionModel): DataFrame = {
    model.transform(data)
  }

  def getPreparedPredictData(spark: SparkSession): DataFrame = {
    val v1 = Vectors.dense(1.6, 2.3, 4.6)
    val v2 = Vectors.dense(2.6, 3.3, 5.6)
    val v3 = Vectors.dense(3.6, 4.3, 6.6)
    val v4 = Vectors.dense(4.6, 5.3, -7.6)
    val v5 = Vectors.dense(5.6, 6.3, -8.6)
    val v6 = Vectors.dense(6.6, 7.3, -9.6)
    val dataInit = Array(v1, v2, v3, v4, v5, v6)
    spark.createDataFrame(dataInit.map(Tuple1.apply)).toDF("features")
  }

  def generateModel(dataTrain: DataFrame): LogisticRegressionModel = {
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    lr.fit(dataTrain)
  }

  def featuresProject(spark: SparkSession): DataFrame = {
    val v1 = Vectors.dense(4.6, 4.3, -4.6)
    val v2 = Vectors.dense(2.6, -3.3, 5.6)
    val v3 = Vectors.dense(3.6, -4.3, -6.6)
    val v4 = Vectors.dense(4.6, 5.3, -1.9)
    val v5 = Vectors.dense(5.6, 6.3, -2.7)
    val v6 = Vectors.dense(6.6, 7.3, -0.7)

    val dataInit = Array(
      (0, v1)
      , (0, v2)
      , (0, v3)
      , (1, v4)
      , (1, v5)
      , (1, v6)
    )

    spark.createDataFrame(dataInit).toDF("label", "features")
  }
}
