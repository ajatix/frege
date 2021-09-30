package frege.core.engine

import frege.{Id, Rule}

case class EvaluationContext(
    rules: Map[Id, Rule],
    negativeRules: Map[Id, Rule]
) {
  val positiveTargets: Map[Id, Int] =
    rules.view.mapValues(_.applicable.size).toMap
  val negativeTargets: Map[Id, Int] =
    negativeRules.view.mapValues(_.applicable.size).toMap
}
