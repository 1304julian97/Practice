package com.practice.cache.domain.cachesettings

import com.github.blemale.scaffeine
import com.github.blemale.scaffeine.Scaffeine
import com.practice.cache.domain.Entities.Person

import scala.concurrent.duration.{DurationInt, FiniteDuration}


case object CaffeineCacheImplementation extends Cache[Int, List[Person]] {


  private var optionalTTL: Option[FiniteDuration] = None

  def expireAfterCreate(key: Int, value: List[Person]): FiniteDuration = {
    println("Eyyy I am creating an expire rule"+key+" ____  "+value)
    println(s"Optional TTL value is:  ${optionalTTL.orNull}")
    optionalTTL.getOrElse {
      if (key == 1997) 10.seconds
      else 60.seconds
    }
  }

  def expireAfterUpdate(key: Int, value: List[Person], currentDuration: FiniteDuration): FiniteDuration = {
    currentDuration / 2
  }

  def expireAfterRead(key: Int, value: List[Person], currentDuration: FiniteDuration): FiniteDuration = {
    println(s"Current TTL of $key and $value is: ${currentDuration.toSeconds}")
    currentDuration
  }




  override val generalMaxTimeToLeave: FiniteDuration = 3.minutes

  val cache: scaffeine.Cache[Int, List[Person]] = Scaffeine().recordStats().
    expireAfter[Int, List[Person]](expireAfterCreate, expireAfterUpdate, expireAfterRead).
    build[Int, List[Person]]()


  override def save(key:Int, a: List[Person], timeToLeaveMS: Option[FiniteDuration]): Unit = {
    optionalTTL = timeToLeaveMS
    cache.put(key, a)
  }

  override def get(key: Int): Option[List[Person]] = cache.getIfPresent(key)

  override def delete(key: Int): Option[List[Person]] = {
    val people = get(key)
    cache.invalidate(key)
    people
  }
}
