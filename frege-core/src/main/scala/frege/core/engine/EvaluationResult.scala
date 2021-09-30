package frege.core.engine

case class EvaluationResult(
    applicable: Set[Int],
    blockedBy: Set[Int] = Set.empty
)
