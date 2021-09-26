package ninja.scala.frege.core.engine

import ninja.scala.frege.{Id, Rule}

case class EvaluationContext(
    rules: Map[Id, Rule],
    negativeRules: Map[Id, Rule]
) {
  val positiveTargets: Map[Id, Int] = rules.mapValues(_.applicable.size)
  val negativeTargets: Map[Id, Int] = negativeRules.mapValues(_.applicable.size)
}
