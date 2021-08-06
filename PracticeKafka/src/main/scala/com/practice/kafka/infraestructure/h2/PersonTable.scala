package com.practice.kafka.infraestructure.h2

import slick.jdbc.H2Profile.api._


class PersonTable( tag: Tag) extends Table[PersonDAORecord](tag,"person"){

  def name = column[String]("name", O.PrimaryKey)
  def lastName = column[String]("lastName")
  def year = column[Int]("year")

  override def * = (name,lastName,year) <> (PersonDAORecord.tupled, PersonDAORecord.unapply)
}
