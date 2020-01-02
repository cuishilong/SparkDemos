package com.doumi.biz.ml.demos

import com.doumi.biz.util.ScalaUtil
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

    val Array(trainData, predictData) = data.randomSplit(Array(0.6, 0.4))

    val model = generateModel(trainData)

    model
      .transform(predictData)
      .orderBy("probability")
      .show(1000)

    val summarize = model.summary
    println(summarize.accuracy)
    println(s"Coefficients: \n${model.coefficientMatrix}")
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
    spark
      .read
      .format("libsvm")
      .load(s"${ScalaUtil.getCurrentDir}/data/sample_multiclass_classification_data.txt")
  }
}
