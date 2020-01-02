package com.doumi.biz.ml.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

object MLLogicRegressionDemo {

  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMastre = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMastre)
      .getOrCreate()

    val data = spark
      .read
      .format("libsvm")
      .load(s"file://${ScalaUtil.getCurrentDir}/data/sample_libsvm_data.txt")

    val Array(trainData, testData) = data.randomSplit(Array(0.7, 0.3))

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)
//      .setFamily("multinomial")

    val model = lr.fit(trainData)

    println(model.intercept)

    model.transform(testData.select("features")).show()
    model.summary.precisionByLabel.foreach(println(_))

    println(model.summary.accuracy)
    println(s"Coefficients: \n${model.coefficientMatrix}")

//    model.save(s"file://${ScalaUtil.getCurrentDir}/model/logistic_regression_model")

    spark.stop()
  }
}
