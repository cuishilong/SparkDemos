package com.doumi.biz.mllib.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object MLlinearSVM {
  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMaster = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMaster)
      .getOrCreate()

    val sc = spark.sparkContext

    val featuresRdd = MLUtils.loadLibSVMFile(sc, s"file://${ScalaUtil.getCurrentDir}/data/sample_libsvm_data.txt")

    val Array(trainData, testData) = featuresRdd.randomSplit(Array(0.6, 0.4))

    val model = SVMWithSGD.train(trainData, 10)
    model.clearThreshold()

    testData.map(point => {
      val score = model.predict(point.features)
      (score, point.label)
    })
      .collect()
      .foreach(println(_))

    spark.stop()
  }
}
