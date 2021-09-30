package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import frege.{Request, SimpleRule}
import frege.core.engine._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EvaluatorSpec extends AnyFlatSpec with Matchers {

  behavior of "evaluator"

  private val debug: Boolean = true

  val requestGenerator = new RequestGenerator()
  val ruleGenerator = new RuleGenerator(0.3)

  val (rules, negativeRules) = ruleGenerator.generate(2)
  val requests: Seq[Request] = requestGenerator.generate(2)

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
