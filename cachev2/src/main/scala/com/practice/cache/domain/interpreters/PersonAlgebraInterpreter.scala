package com.practice.cache.domain.interpreters

import cats.Applicative
import cats.data.EitherT
import cats.implicits.catsSyntaxApplicativeId
import com.practice.cache.domain.{Entities, PersonAlgebra}

class PersonAlgebraInterpreter[F[_]:Applicative] extends PersonAlgebra[F] {
  override def filterUser(name: String, exactFilter: Boolean, people: List[Either[String,Entities.Person]]): EitherT[F, String, List[Entities.Person]] = {


    val (messagesErrors, peopleRight) = people.foldRight[(List[String],List[Entities.Person])](Nil,Nil) {
      case (Left(message), (leftAcumulated,rightAcumulated)) => (message :: leftAcumulated,rightAcumulated)
      case (Right(person),(leftAcumulated,rightAcumulated)) => (leftAcumulated, person :: rightAcumulated)
    }

    val f1 = messagesErrors.toSet.toList
    val f2: List[Entities.Person] = peopleRight.filter( p => if(exactFilter) p.name.toLowerCase == name.toLowerCase else p.name.toLowerCase.contains(name.toLowerCase))

    val finalResponse: Either[String, List[Entities.Person]] = (f1, f2) match {
      case (Nil,Nil) => Left("Do not exist people in DB")
      case (List(_*), Nil) => Left(f1.mkString(","))
      case (_ , List(_*)) => Right(f2)
    }
    EitherT(finalResponse.pure)

  }


/*
  trait MailServer{
    def sendEmailAdmin(message:String):Boolean
  }

  def sendEmail(list: List[Either[String,Int]])(mailServer: MailServer):Either[String,List[Int] ]={

    val (messages,prices) = list.foldRight[(List[String],List[Int])](Nil,Nil){
      case (Left(message), (leftAcumulated,rightAcumulated)) => (message :: leftAcumulated,rightAcumulated)
      case (Right(price),(leftAcumulated,rightAcumulated)) => (leftAcumulated, price :: rightAcumulated)
    }

    val emailSent = messages.map(mailServer.sendEmailAdmin)

    if(emailSent.isEmpty || emailSent.forall(_ == true)) Right(prices)
    else Left("No email sent")

  }


  def validatePrices(list:List[Either[String,Int]], reference:Int): List[Either[String,Int]] = {

    list.
  }

*/

















}
