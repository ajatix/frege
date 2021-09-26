package ninja.scala.frege

sealed trait Action

final case class Lift(percent: Double) extends Action
