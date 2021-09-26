package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

case class EvaluationResult(
    applicable: Set[Int],
    ruleMap: Map[Id, Boolean] = Map.empty,
    positive: Map[Id, Boolean] = Map.empty,
    negative: Map[Id, Boolean] = Map.empty
)

object EvaluationResult {

  def apply(
      ruleResult: RuleResult
  ): EvaluationResult = {
    /*
    val positive =
      ruleResult.getPositive.map(id => id -> filterPositive.contains(id)).toMap
    val negative =
      ruleResult.getNegative.map(id => id -> filterNegative.contains(id)).toMap
    val ruleMap = ctx.rules.keys
      .map(id =>
        id -> (filterPositive.contains(id) && !filterNegative.contains(id))
      )
      .toMap
     */
    val applicable = ruleResult.getApplicable
    EvaluationResult(applicable)
  }
}
