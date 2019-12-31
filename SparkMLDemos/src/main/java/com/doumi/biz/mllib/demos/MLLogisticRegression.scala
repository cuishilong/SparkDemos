package com.doumi.biz.mllib.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object MLLogisticRegression {
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

    val path = s"file://${ScalaUtil.getCurrentDir}/data/sample_libsvm_data.txt"
    val data = MLUtils.loadLibSVMFile(sc, path)
    val Array(trainData, testData) = data.randomSplit(Array(0.7, 0.3))

    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(10)
      .run(trainData)

    val predictionAndLabels = testData.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    predictionAndLabels.toDF("prediction", "label").show()

    val metrics = new MulticlassMetrics(predictionAndLabels)
    val accuracy = metrics.accuracy

    println(accuracy)

    spark.stop()
  }
}
