package ninja.scala.frege.core.engine

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
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
    negativeRuleMap: Int2ObjectOpenHashMap[Set[Int]],
    metadata: GraphMetadata
)

class GraphEvaluationContextBuilder(implicit ctx: EvaluationContext) {

  private def fieldMap(): Object2ObjectMap[Field, RuleResult] =
    new Object2ObjectOpenHashMap[Field, RuleResult]()

  val graph: Object2ObjectMap[String, Object2ObjectMap[Field, RuleResult]] =
    new Object2ObjectOpenHashMap[String, Object2ObjectMap[Field, RuleResult]]()

  val negativeRuleMap: Int2ObjectOpenHashMap[Set[Int]] =
    new Int2ObjectOpenHashMap[Set[Int]]()

  def addRules(rules: Map[Id, Rule]): Unit = {
    rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
      val positiveTarget = (1 << positive.size) - 1
      positive.zipWithIndex.foreach { case (Segment(_, feature, fences), idx) =>
        val featureGraph = graph.getOrDefault(feature.name, fieldMap())
        fences.foreach { fence =>
          val fenceRuleResult =
            featureGraph.getOrDefault(fence.v, new RuleResult())
          fenceRuleResult.addPositive(
            ruleId,
            new Partial(1 << idx, positiveTarget)
          )
          featureGraph.put(fence.v, fenceRuleResult)
        }
        graph.put(feature.name, featureGraph)
      }
      val negativeIds = Set.newBuilder[Int]
      negative.foreach { case SimpleRule(id, _, _, positive, _) =>
        negativeIds += id
        val negativeTarget = (1 << positive.size) - 1
        positive.zipWithIndex.foreach {
          case (Segment(_, feature, fences), idx) =>
            val featureGraph = graph.getOrDefault(feature.name, fieldMap())
            fences.foreach { fence =>
              val fenceRuleResult =
                featureGraph.getOrDefault(fence.v, new RuleResult())
              fenceRuleResult.addNegative(
                id,
                new Partial(1 << idx, negativeTarget)
              )
              featureGraph.put(fence.v, fenceRuleResult)
            }
            graph.put(feature.name, featureGraph)
        }
      }
      negativeRuleMap.put(ruleId, negativeIds.result())
    }
  }

  def build(): GraphEvaluationContext = {
    addRules(ctx.rules)
    val graphMetadata = GraphMetadata(
      numFeatures = graph.size,
      dimensions = Map.empty,
      rules = Map.empty
    )
    GraphEvaluationContext(graph, negativeRuleMap, graphMetadata)
  }

}
