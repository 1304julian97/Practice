package com.practice.cache.domain.cachesettings

import scala.concurrent.duration.FiniteDuration

trait Cache[K,A] {

  val generalMaxTimeToLeave:FiniteDuration

  def save(key: K, a:A, timeToLeaveMS:Option[FiniteDuration])

  def get(key:K):Option[A]

  def delete(key:K):Option[A]


}
