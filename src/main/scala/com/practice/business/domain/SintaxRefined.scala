package com.practice.business.domain

import com.practice.business.domain.Models.{LastName, RestrictionSizeInt, RestrictionSizeString}
import eu.timepit.refined.refineV
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.Validate

import scala.math.Numeric.IntIsIntegral

object SintaxRefined {



  implicit class RefinedString(s:String){
    def toRefined[R]: Either[String, Refined[String, RestrictionSizeString]] =
      refineV[RestrictionSizeString](s) match{
        case Left(value) => Left(s"Impossible to define $s with error $value")
        case r @ Right(_) => r
      }
  }

  implicit class RefinedT[T,R](t:T){
    def validateWith[R](implicit v:Validate[T,R]): Either[String, Refined[T, R]] =
      refineV[R](t) match{
        case Left(value) => Left(s"Impossible to define $t with error $value")
        case r @ Right(_) => r
      }
  }

  implicit class RefinedInt(i:Int){
    def toRefined: Either[String, Refined[Int, RestrictionSizeInt]] = refineV[RestrictionSizeInt](i) match{
      case Left(value) => Left(s"Impossible to define $i with error $value")
      case r @ Right(_) => r
    }
  }


  implicit def fromIntToRefined(i:Int): Either[String, Refined[Int, RestrictionSizeInt]] = refineV[RestrictionSizeInt](i) match{
    case Left(value) => Left(s"Impossible to define $i with error $value")
    case r @ Right(_) => r
  }

  implicit def fromStringTo(s:String): Either[String, Refined[String, RestrictionSizeString]] =
    refineV[RestrictionSizeString](s) match{
      case Left(value) => Left(s"Impossible to define $s with error $value")
      case r @ Right(_) => r
    }

  


}
