package ninja.scala.frege.core.engine

import ninja.scala.frege._

import scala.collection.mutable

case class GraphMetadata(
    numFeatures: Int,
    dimensions: Map[String, Int],
    rules: Map[String, Int]
)
case class GraphEvaluationContext(
    graph: Map[String, Map[Field, RuleResult]],
    metadata: GraphMetadata
)

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
    val graphMetadata = GraphMetadata(
      numFeatures = graph.size,
      dimensions = graph.mapValues(f => f.size).toMap,
      rules = graph.mapValues { f =>
        val sum = f.foldLeft(new RuleResult) { case (acc, (_, el)) =>
          el.getPositive.foreach(acc.addPositive)
          el.getNegative.foreach(acc.addNegative)
          acc
        }
        sum.getPositive.size + sum.getNegative.size
      }.toMap
    )
    GraphEvaluationContext(graph.mapValues(_.toMap).toMap, graphMetadata)
  }

}
