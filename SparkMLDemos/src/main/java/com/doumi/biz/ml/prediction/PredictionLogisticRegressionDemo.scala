package com.doumi.biz.ml.prediction

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object PredictionLogisticRegressionDemo {
  val tag = getClass.getSimpleName.replaceAll("\\$", "")
  val master = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(tag)
      .master(master)
      .getOrCreate()

    val modelPath = s"${ScalaUtil.getCurrentDir}/model/logistic_regression_model"
    val model = LogisticRegressionModel.load(modelPath)

    val vector1 = Vectors.sparse(692, Seq((1, 4.5), (0, 3.9)))
    val vector2 = Vectors.sparse(692, Seq((1, 4.5), (0, 3.9)))
    val vector3 = Vectors.sparse(692, Seq((1, 4.5), (0, 3.9)))

    val testData = Array(vector1,vector2,vector3)

    val test = spark
      .createDataFrame(testData.map(Tuple1.apply))
      .toDF("features")

    model.transform(test).show()

    spark.stop()

  }
}
