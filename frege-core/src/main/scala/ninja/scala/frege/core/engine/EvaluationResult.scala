package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

case class EvaluationResult(ruleMap: Map[Id, Boolean])

object EvaluationResult {

  def apply(ruleResult: RuleResult): EvaluationResult = {
    val resolveRules = ruleResult.getPositive
      .map(id => id -> !ruleResult.getNegative.contains(id))
      .toMap
    EvaluationResult(resolveRules)
  }
}
