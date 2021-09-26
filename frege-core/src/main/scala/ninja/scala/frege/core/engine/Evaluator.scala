package ninja.scala.frege.core.engine

import ninja.scala.frege.Request

sealed trait Evaluator {
  def eval(request: Request): EvaluationResult
}

class StandardEvaluator(implicit ctx: EvaluationContext) extends Evaluator {
  override def eval(request: Request): EvaluationResult = {
    EvaluationResult(ctx.rules.collect {
      case (ruleId, rule) if rule.eval(request) => ruleId
    }.toSet)
  }
}

class GraphEvaluator(implicit
    ctx: EvaluationContext,
    gtx: GraphEvaluationContext
) extends Evaluator {

  override def eval(request: Request): EvaluationResult = {
    val ruleResult = gtx.graph
      .flatMap { case (feature, dimensions) =>
        request.get(feature).flatMap(dimensions.get)
      }
      .foldLeft(new RuleResult) { case (acc, el) =>
        acc.add(el)
        acc
      }
    EvaluationResult(ruleResult)
  }
}
