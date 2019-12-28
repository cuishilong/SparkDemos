package com.doumi.biz.kafka

import java.util.Properties

import com.alibaba.fastjson.JSONObject
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object DataProducer {
  def main(args: Array[String]): Unit = {

    val kafkaprop = new Properties()
    kafkaprop.setProperty("bootstrap.servers", "localhost:9092")
    kafkaprop.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaprop.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](kafkaprop)

    for (i <- 0 to 1000 * 10000) {
      val jsonObj = new JSONObject()
      jsonObj.put("id", i)
      jsonObj.put("name", "arica")

      val record = new ProducerRecord[String, String]("test", s"${i}", jsonObj.toString())
      producer.send(record)
      //      println(record.toString)
      Thread.sleep(3)
    }
  }
}
