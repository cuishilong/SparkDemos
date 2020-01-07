package com.doumi.biz.ml.demos

import com.doumi.biz.util.ScalaUtil
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.{SaveMode, SparkSession}

object MLTfIdfDemo {
  val tag = getClass.getSimpleName.replaceAll("\\$", "")
  val master = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(tag)
      .master(master)
      .getOrCreate()

    val dataInit = Seq(
      (0.0, "Hi I heard about Spark"),
      (0.0, "I wish Java could use case classes"),
      (1.0, "Logistic regression models are neat are")
    )

    val dataText = spark.createDataFrame(dataInit).toDF("label", "line")

    val tokenizer = new Tokenizer().setInputCol("line").setOutputCol("words")

    val wordsData = tokenizer.transform(dataText)

    val hashingTF = new HashingTF()
      .setInputCol("words")
      .setOutputCol("rawFeatures")
      // Vector长度
      .setNumFeatures(100)

    wordsData.show()

    val featuresData = hashingTF.transform(wordsData)

    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")

    val idfModel = idf.fit(featuresData)

    featuresData
      .coalesce(1)
      .selectExpr("label", "rawFeatures as features")
      .write
      .format("libsvm")
      .mode(SaveMode.Overwrite)
      .save(s"file://${ScalaUtil.getCurrentDir}/data/tf_features.txt")

    featuresData.show()
    val df = idfModel
      .transform(featuresData.select("rawFeatures"))
    df.show()

    spark.stop()
  }
}
