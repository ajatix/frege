package ninja.scala.frege.core.engine

import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectOpenHashMap
}
import ninja.scala.frege._

case class GraphMetadata(
    numFeatures: Int,
    dimensions: Map[String, Int],
    rules: Map[String, Int]
)
case class GraphEvaluationContext(
    graph: Object2ObjectMap[String, Object2ObjectMap[Field, RuleResult]],
    metadata: GraphMetadata
)

class GraphEvaluationContextBuilder(implicit ctx: EvaluationContext) {

  private def fieldMap(): Object2ObjectMap[Field, RuleResult] =
    new Object2ObjectOpenHashMap[Field, RuleResult]()

  val graph: Object2ObjectMap[String, Object2ObjectMap[Field, RuleResult]] =
    new Object2ObjectOpenHashMap[String, Object2ObjectMap[Field, RuleResult]]()

  def addRules(rules: Map[Id, Rule]): Unit = {
    rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
      val positiveSegmentWeightage = 1.0f / positive.size
      positive.foreach { case Segment(_, feature, fences) =>
        val featureGraph = graph.getOrDefault(feature.name, fieldMap())
        fences.foreach { fence =>
          val fenceRuleResult =
            featureGraph.getOrDefault(fence.v, new RuleResult())
          fenceRuleResult.addPositive(ruleId, positiveSegmentWeightage)
          featureGraph.put(fence.v, fenceRuleResult)
        }
        graph.put(feature.name, featureGraph)
      }
      negative.foreach { case SimpleRule(id, _, _, positive, _) =>
        val negativeSegmentWeightage = 1.0f / positive.size
        positive.foreach { case Segment(_, feature, fences) =>
          val featureGraph = graph.getOrDefault(feature.name, fieldMap())
          fences.foreach { fence =>
            val fenceRuleResult =
              featureGraph.getOrDefault(fence.v, new RuleResult())
            fenceRuleResult.addNegative(id, ruleId, negativeSegmentWeightage)
            featureGraph.put(fence.v, fenceRuleResult)
          }
          graph.put(feature.name, featureGraph)
        }
      }
    }
  }

  def build(): GraphEvaluationContext = {
    addRules(ctx.rules)
    val graphMetadata = GraphMetadata(
      numFeatures = graph.size,
      dimensions = Map.empty,
      rules = Map.empty
    )
    GraphEvaluationContext(graph, graphMetadata)
  }

}
