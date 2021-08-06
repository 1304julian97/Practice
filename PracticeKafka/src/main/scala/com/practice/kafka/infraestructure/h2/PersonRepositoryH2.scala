package com.practice.kafka.infraestructure.h2

import com.practice.kafka.domain.{Models, PersonRepository}
import monix.eval.Task
import slick.jdbc.H2Profile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

class PersonRepositoryH2 extends PersonRepository with PersonAccountDAOMapper {

  val db = Database.forConfig( "h2.default" )
  private val action = H2Tables.personTable.schema.create
  Await.ready( db.run( action ), 5.seconds )

  override def save(person: Models.Person): Task[Either[String, Models.Person]] = {

    val personDAO = personDomain2DAO(person)
    val dbAction = H2Tables.personTable.+=(personDAO)
    Task.
      fromFuture(
        db.run(dbAction)
      ).
      map(_ => Right(person)).
      onErrorHandle( _ => Left(s"An error was found saving in DB the person $person"))
  }

  override def getAll: Task[Either[String, List[Models.Person]]] = {
    val action = H2Tables.personTable.result
    Task.deferFuture{
      db.run(action).map(records=> records.toList.map(personDAO2Domain))
    }.map(x => Right(x)).onErrorHandle(e => Left(e.toString))
  }
}
