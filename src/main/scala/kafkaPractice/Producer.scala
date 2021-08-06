package kafkaPractice

import java.util.Properties
import org.apache.kafka.clients.producer._


trait Producer {


  def writeToKafka(topic: String): Unit = {

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.ACKS_CONFIG, "0")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "julian", "hola "+topic)
    val messageSent = producer.send(record)

    while(!messageSent.isDone){
      println("no he acabado "+topic)
    }
    messageSent.get().offset()

    producer.close()
    println("acabé")
  }
}