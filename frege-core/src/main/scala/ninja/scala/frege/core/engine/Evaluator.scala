package ninja.scala.frege.core.engine

import ninja.scala.frege.Request

sealed trait Evaluator {
  def eval(request: Request): EvaluationResult
}

class StandardEvaluator(implicit ctx: EvaluationContext) extends Evaluator {
  override def eval(request: Request): EvaluationResult = {
    EvaluationResult(ctx.rules.mapValues(_.eval(request)))
  }
}

class GraphEvaluator(implicit gtx: GraphEvaluationContext) extends Evaluator {

  override def eval(request: Request): EvaluationResult = {
    val ruleResult = gtx.graph
      .flatMap { case (feature, dimensions) =>
        request.get(feature).flatMap(dimensions.get)
      }
      .foldLeft(new RuleResult) { case (x, y) =>
        y.getPositive.foreach(x.addPositive)
        y.getNegative.foreach(x.addNegative)
        x
      }
    EvaluationResult(ruleResult)
  }
}
