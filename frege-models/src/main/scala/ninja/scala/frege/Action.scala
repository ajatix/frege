package ninja.scala.frege

sealed trait Action

case object Skip extends Action

final case class Lift(percent: Double) extends Action with HasPercent
