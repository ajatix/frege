package ninja.scala.frege.core.engine

import ninja.scala.frege.{Id, Rule}

case class EvaluationContext(rules: Map[Id, Rule])
