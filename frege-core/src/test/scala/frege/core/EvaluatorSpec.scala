package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import frege.Request
import frege.core.engine._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EvaluatorSpec extends AnyFlatSpec with Matchers {

  behavior of "evaluator"

  private val debug: Boolean = true

  val requestGenerator = new RequestGenerator()
  val ruleGenerator = new RuleGenerator(0.3)

  val (rules, negativeRules) = ruleGenerator.generate(5)
  val requests: Seq[Request] = requestGenerator.generate(1)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  if (debug) {
    println("[rules]")
    rules.foreach(println)

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
