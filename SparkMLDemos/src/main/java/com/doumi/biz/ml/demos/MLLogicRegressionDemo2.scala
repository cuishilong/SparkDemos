package com.doumi.biz.ml.demos

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object MLLogicRegressionDemo2 {

  val streamTag = getClass.getSimpleName.replaceAll("\\$", "")
  val streamMastre = "local[*]"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(streamTag)
      .master(streamMastre)
      .getOrCreate()

    val v1 = Vectors.sparse(3, Seq((0, 1.0), (1, 2.0), (2, 3.0)))
    val v2 = Vectors.sparse(3, Seq((0, 2.0), (1, 3.0), (2, 4.0)))
    val v3 = Vectors.sparse(3, Seq((0, 3.0), (1, 6.0), (2, 9.0)))
    val preV1 = Vectors.dense(5.0, 6.0, 7.0)
    val preV2 = Vectors.dense(9.0, 10.0, 11.0)
    val preV3 = Vectors.dense(2.0, 4.0, 6.0)
    val preV4 = Vectors.dense(4.0, 8.0, 12.0)
    val data = Array(
      (0, v1)
      , (0, v2)
      , (1, v3)
      , (0, preV1)
      , (0, preV2)
      , (1, preV3)
      , (1, preV4)
    )

    val trainData = spark.createDataFrame(data).toDF("label", "features")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val model = lr.fit(trainData)


    val data2 = Array(preV1, preV2, preV3)

    val predictData = spark.createDataFrame(data2.map(Tuple1.apply)).toDF("features")

    model.transform(predictData).show()

    println(model.coefficientMatrix)

    spark.stop()
  }
}
