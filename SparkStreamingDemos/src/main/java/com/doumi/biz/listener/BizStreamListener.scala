package com.doumi.biz.listener

import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted}

class BizStreamListener(streamTag:String) extends StreamingListener {
  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    val info = batchCompleted.batchInfo
    println(s"numRecords : ${info.numRecords}")
  }
}
