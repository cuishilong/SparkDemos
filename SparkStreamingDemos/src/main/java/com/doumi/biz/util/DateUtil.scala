package com.doumi.biz.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object DateUtil {
  def main(args: Array[String]): Unit = {
    println(getDiffDateStr("yyyyMMddHHmm", 1))
  }

  def getDiffDateStr(format: String, diff: Int): String = {
    val date = getDate
    val cal = Calendar.getInstance()
    cal.setTime(date)
    cal.add(Calendar.DATE, diff)
    val sdf = new SimpleDateFormat(format)
    sdf.format(cal.getTime)
  }

  def getDate = new Date()
}
