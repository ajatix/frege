package frege.core

import frege.core.engine._
import frege.generators.RequestGenerator
import frege.syntax._
import frege._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PMMLEvaluatorSpec extends AnyFlatSpec with Matchers {

  behavior of "pmml evaluator"

  private val debug: Boolean = true

  val requestGenerator = new RequestGenerator()

  val negativeRules: Map[Id, Rule] = Map.empty
  val rules: Map[Id, Rule] = Map(
    1 -> SimpleRule(
      1,
      name = "pmml-1",
      action = Lift(-0.175),
      positive = Set(
        Segment(1, Feature(6, "margin_percent"), fences = Set(Fence.lteq(10)))
      )
    ),
    2 -> SimpleRule(
      2,
      name = "pmml-1",
      action = Lift(0.104),
      positive = Set(
        Segment(1, Feature(6, "margin_percent"), fences = Set(Fence.gt(10))),
        Segment(2, Feature(3, "logged_in"), fences = Set(Fence.neq(true)))
      )
    ),
    3 -> SimpleRule(
      3,
      name = "pmml-1",
      action = Lift(0.043),
      positive = Set(
        Segment(
          1,
          Feature(6, "margin_percent"),
          fences = Set(Fence.in(10, 14))
        ),
        Segment(2, Feature(3, "logged_in"), fences = Set(Fence.eq(true)))
      )
    ),
    4 -> SimpleRule(
      4,
      name = "pmml-1",
      action = Lift(0.022),
      positive = Set(
        Segment(1, Feature(6, "margin_percent"), fences = Set(Fence.gt(16))),
        Segment(2, Feature(3, "logged_in"), fences = Set(Fence.eq(true)))
      )
    ),
    5 -> SimpleRule(
      5,
      name = "pmml-1",
      action = Lift(-0.051),
      positive = Set(
        Segment(
          1,
          Feature(6, "margin_percent"),
          fences = Set(Fence.in(14, 16))
        ),
        Segment(2, Feature(3, "logged_in"), fences = Set(Fence.eq(true)))
      )
    )
  )

  val requests: Seq[Request] = requestGenerator.generate(1)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  if (debug) {
    println("[negative rules]")
    negativeRules.foreach { case (ruleId, rule) =>
      println(s"$ruleId, ${rule.name}, ${rule.action}")
      println("applicable")
      rule.applicable.foreach(println)
    }

    println("[rules]")
    rules.foreach { case (ruleId, rule: SimpleRule) =>
      println(s"$ruleId, ${rule.name}, ${rule.action}")
      println("positive")
      rule.positive.foreach(println)
      println("negative")
      println(rule.negative.map(_.name))
    }

    println("[graph]")
    gtx.graph.forEach { (feature, nodes) =>
      println(feature)
      nodes.forEach { (node, result) =>
        println(node, result)
      }
    }

    println("[graph negativeRuleMap]")
    println(gtx.negativeRuleMap)

    println("[graph metadata]")
    println(gtx.metadata)

    println("[requests]")
    requests.foreach(println)
  }

  it should "compute standard" in {
    val standardResponse =
      requests
        .map(request => request -> standardEvaluator.eval(request).applicable)
        .toMap
    val graphResponse =
      requests
        .map(request => request -> graphEvaluator.eval(request).applicable)
        .toMap

    if (debug) {
      println("[standard response]")
      println(standardResponse)

      println("[graph response]")
      println(graphResponse)
    }

    graphResponse shouldEqual standardResponse
  }

}
