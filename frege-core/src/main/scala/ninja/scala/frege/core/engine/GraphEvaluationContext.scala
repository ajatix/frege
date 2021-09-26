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

  private def fieldMap: Object2ObjectMap[Field, RuleResult] = {
    val instance = new Object2ObjectOpenHashMap[Field, RuleResult] {}
    instance.defaultReturnValue(new RuleResult())
    instance
  }

  private val graph
      : Object2ObjectMap[String, Object2ObjectMap[Field, RuleResult]] = {
    val instance = new Object2ObjectOpenHashMap[
      String,
      Object2ObjectMap[Field, RuleResult]
    ]()
    instance.defaultReturnValue(fieldMap)
    instance
  }

  def addRules(rules: Map[Id, Rule]): Unit = {

    rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
      positive.foreach { case Segment(_, feature, fences) =>
        val featureGraph = graph.get(feature.name)
        fences.foreach { fence =>
          featureGraph.get(fence.v).addPositive(ruleId)
        }
      }
      negative.foreach { case SimpleRule(_, _, _, positive, _) =>
        positive.foreach { case Segment(_, feature, fences) =>
          val featureGraph = graph.get(feature.name)
          fences.foreach { fence =>
            featureGraph.get(fence.v).addNegative(ruleId)
          }
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
