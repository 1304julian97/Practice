package kafkaPractice

import monix.eval.Task

import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{ExecutionContext, Future}


object MainKafkaConsumer extends App with Consumer with Producer {


  val x: Future[Unit] = consumeFromKafka1("person-topic")

  while(true){}

}

object MainKafkaProducer extends App with Consumer with Producer {


  writeToKafka("julian-topic2")
  writeToKafka("julian-topic1")

}
