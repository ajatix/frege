package frege.core.engine

import frege.Request

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

class GraphEvaluator(implicit gtx: GraphEvaluationContext) extends Evaluator {

  override def eval(request: Request): EvaluationResult = {
    val ruleResult = new RuleResult()
    gtx.graph.forEach((k, v) => {
      request.get(k).foreach { field =>
        if (v.containsKey(field)) ruleResult.add(v.get(field))
      }
    })
    ruleResult.getApplicable
  }
}
