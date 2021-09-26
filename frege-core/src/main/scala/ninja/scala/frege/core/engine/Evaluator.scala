package ninja.scala.frege.core.engine

import ninja.scala.frege.{Field, Request, Segment, SimpleRule}

import scala.collection.mutable

sealed trait Evaluator {
  def eval(request: Request): EvaluationResult
}

class StandardEvaluator(implicit ctx: EvaluationContext) extends Evaluator {
  override def eval(request: Request): EvaluationResult = {
    EvaluationResult(ctx.rules.mapValues(_.eval(request)))
  }
}

class GraphEvaluator(implicit ctx: EvaluationContext) extends Evaluator {

  private val graph =
    mutable.HashMap.empty[String, mutable.HashMap[Field, RuleResult]]
  ctx.rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
    positive.foreach { case Segment(_, feature, fences) =>
      if (!graph.contains(feature.name))
        graph.put(feature.name, mutable.HashMap.empty[Field, RuleResult])
      graph.get(feature.name).foreach { featureGraph =>
        fences.foreach { fence =>
          if (!featureGraph.contains(fence.v))
            featureGraph.put(fence.v, new RuleResult)
          featureGraph.get(fence.v).foreach(_.addPositive(ruleId))
        }
      }
    }
    negative.foreach { case SimpleRule(_, _, _, positive, _) =>
      positive.foreach { case Segment(_, feature, fences) =>
        if (!graph.contains(feature.name))
          graph.put(feature.name, mutable.HashMap.empty[Field, RuleResult])
        graph.get(feature.name).foreach { featureGraph =>
          fences.foreach { fence =>
            if (!featureGraph.contains(fence.v))
              featureGraph.put(fence.v, new RuleResult)
            featureGraph.get(fence.v).foreach(_.addNegative(ruleId))
          }
        }
      }
    }
  }

  override def eval(request: Request): EvaluationResult = {
    val ruleResult = graph
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
