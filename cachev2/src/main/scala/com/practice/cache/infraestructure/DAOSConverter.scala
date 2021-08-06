package com.practice.cache.infraestructure

import com.practice.cache.domain.Entities

object DAOSConverter {

  def personDAO2Domain(dao:DAOS.Person):Either[String,Entities.Person] = {
    Entities.Person(dao.name,dao.lastName,dao.year)
  }

}
