package com.practice.kafka.infraestructure
import com.practice.kafka.domain.PersonRepository
import com.practice.kafka.infraestructure.h2.PersonRepositoryH2

class ContextInH2 extends Context {
  override val personRepository: PersonRepository = new PersonRepositoryH2
}
