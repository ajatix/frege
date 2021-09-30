package frege.core.engine

import frege._
import it.unimi.dsi.fastutil.ints.{
  Int2ObjectMap,
  Int2ObjectMaps,
  Int2ObjectOpenHashMap
}
import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectMaps,
  Object2ObjectOpenHashMap
}

case class GraphMetadata(
    numFeatures: Int,
    dimensions: Map[String, Int],
    rules: Map[String, Int]
)
case class GraphEvaluationContext(
    graph: Object2ObjectMap[String, Object2ObjectMap[Field, RuleResult]],
    negativeRuleMap: Int2ObjectMap[Set[Int]],
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
            Result.partial(1 << idx, positiveTarget)
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
                Result.partial(1 << idx, negativeTarget)
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
    val dimensions = Seq.newBuilder[(String, Int)]
    val rules = Seq.newBuilder[(String, Int)]
    graph.forEach { (feature, nodes) =>
      val applicable = Set.newBuilder[Int]
      dimensions += feature -> nodes.size()
      nodes.forEach { (_, result) =>
        applicable ++= result.all()
      }
      rules += feature -> applicable.result().size
    }
    val graphMetadata = GraphMetadata(
      numFeatures = graph.size(),
      dimensions = dimensions
        .result()
        .sortBy { case (feature, numDimensions) =>
          (-1 * numDimensions, feature)
        }
        .toMap,
      rules = rules
        .result()
        .sortBy { case (feature, numRules) =>
          (-1 * numRules, feature)
        }
        .toMap
    )
    // make context immutable
    GraphEvaluationContext(
      Object2ObjectMaps.synchronize(graph),
      Int2ObjectMaps.synchronize(negativeRuleMap),
      graphMetadata
    )
  }

}
