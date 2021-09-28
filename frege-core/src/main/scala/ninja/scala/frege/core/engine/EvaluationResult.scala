package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

case class EvaluationResult(
    applicable: Set[Int],
    ruleMap: Map[Id, Boolean] = Map.empty,
    positive: Map[Id, Boolean] = Map.empty,
    negative: Map[Id, Boolean] = Map.empty
)
