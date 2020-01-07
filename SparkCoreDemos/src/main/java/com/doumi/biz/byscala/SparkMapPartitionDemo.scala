package com.doumi.biz.byscala

import org.apache.spark.{SparkConf, SparkContext}

object SparkMapPartitionDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName(getClass.getSimpleName.replaceAll("\\$", ""))
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(Seq[Int](1, 2, 3, 4, 5, 6, 7, 8, 9, 0))

    rdd.mapPartitions(iter => {
      iter
        .map(e => e * e)
    })
      .collect()
      .foreach(println(_))

    sc.stop()
  }
}
