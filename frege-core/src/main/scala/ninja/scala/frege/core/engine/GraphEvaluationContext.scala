package ninja.scala.frege.core.engine

import ninja.scala.frege._

import scala.collection.mutable

case class GraphEvaluationContext(graph: Map[String, Map[Field, RuleResult]])

class GraphEvaluationContextBuilder(implicit ctx: EvaluationContext) {

  private val graph =
    mutable.HashMap.empty[String, mutable.HashMap[Field, RuleResult]]

  def addRules(rules: Map[Id, Rule]): Unit = {

    rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
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
  }

  def build(): GraphEvaluationContext = {
    addRules(ctx.rules)
    GraphEvaluationContext(graph.mapValues(_.toMap).toMap)
  }

}
