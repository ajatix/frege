package frege.core.engine

import frege.{Field, Request}
import it.unimi.dsi.fastutil.objects.{Object2ObjectMap, Object2ObjectMaps}

import java.util.function.Consumer

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
    val consumer: Consumer[
      Object2ObjectMap.Entry[String, Object2ObjectMap[Field, RuleResult]]
    ] = entry => {
      request.get(entry.getKey).foreach { field =>
        val fieldMap: Object2ObjectMap[Field, RuleResult] = entry.getValue
        if (fieldMap.containsKey(field)) ruleResult.add(fieldMap.get(field))
      }
    }
    Object2ObjectMaps.fastForEach(gtx.graph, consumer)
    ruleResult.getApplicable
  }
}
