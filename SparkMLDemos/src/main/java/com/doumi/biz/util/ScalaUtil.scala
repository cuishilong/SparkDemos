package com.doumi.biz.util

import java.io.{File, FileReader}
import java.util.Properties

object ScalaUtil {
  def main(args: Array[String]): Unit = {
    println(getConf("mysql.url"))
  }
  def getCurrentDir: String = System.getProperty("user.dir")

  def getConf(key: String): String = {
    val fileName = getCurrentDir + "/biz.conf"
    val file = new File(fileName)
    val prop = new Properties()
    prop.load(new FileReader(file))
    prop.getProperty(key)
  }
}
