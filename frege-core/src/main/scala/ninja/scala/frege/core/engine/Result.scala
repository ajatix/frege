package ninja.scala.frege.core.engine

import cats.kernel.Semigroup

import scala.annotation.tailrec

sealed trait Result {
  def add(that: Result): Result
  def isPass: Boolean
  def isFail: Boolean
}

case object Pass extends Result {
  final val isPass: Boolean = true
  final val isFail: Boolean = false

  override def add(that: Result): Result = that match {
    case f @ Fail => f
    case _        => this
  }
}

case object Fail extends Result {
  final val isPass: Boolean = false
  final val isFail: Boolean = true

  override def add(that: Result): Result = this
}

case class Partial(hit: Int, required: Int) extends Result {
  final val isPass: Boolean = false
  final val isFail: Boolean = false

  override def add(that: Result): Result = that match {
    case r @ Fail => r
    case r @ Pass => r // should be ideally impossible to reach this case
    case r @ Partial(thatHit, _) =>
      if (hit + thatHit == required) Pass
      else copy(hit = hit | thatHit)
  }
}
object Partial {
  @tailrec
  def apply(hit: Int, required: Int): Result =
    if (hit == required) Pass else Partial(hit, required)
}

object Result {
  implicit val semigroup: Semigroup[Result] = (x: Result, y: Result) => x add y
}
