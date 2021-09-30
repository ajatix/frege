package frege.core.engine

import frege.{Id, Rule}

case class EvaluationContext(
    rules: Map[Id, Rule],
    negativeRules: Map[Id, Rule]
)
