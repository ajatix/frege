package frege.core.engine

import frege.{Fence, Request}
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
      Object2ObjectMap.Entry[String, Object2ObjectMap[Fence, RuleResult]]
    ] = entry => {
      request.get(entry.getKey).foreach { field =>
        val fenceMap: Object2ObjectMap[Fence, RuleResult] = entry.getValue
        val fenceMapIterator = fenceMap.entrySet().iterator()
        while (fenceMapIterator.hasNext) {
          val nextEntry = fenceMapIterator.next()
          if (nextEntry.getKey.contains(field)) {
            ruleResult.add(nextEntry.getValue)
          }
        }
      }
    }
    Object2ObjectMaps.fastForEach(gtx.graph, consumer)
    ruleResult.getApplicable
  }
}
