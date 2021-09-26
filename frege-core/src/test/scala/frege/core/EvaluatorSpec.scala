package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import ninja.scala.frege.Request
import ninja.scala.frege.core.engine.{
  EvaluationContext,
  GraphEvaluationContext,
  GraphEvaluationContextBuilder,
  GraphEvaluator,
  StandardEvaluator
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EvaluatorSpec extends AnyFlatSpec with Matchers {

  behavior of "evaluator"

  val requestGenerator = new RequestGenerator()
  val ruleGenerator = new RuleGenerator(0.3)

  val (rules, negativeRules) = ruleGenerator.generate(10)
  val requests: Seq[Request] = requestGenerator.generate(2)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  info(s"[rules] ${ctx.rules.size}")
  info(s"[negativeRules] ${ctx.negativeRules}")

  info("[graph]")
  gtx.graph.forEach((feature, dimensions) => {
    info(s"[[$feature]]")
    dimensions.forEach((dimension, ruleResult) => {
      info(s"$dimension : $ruleResult")
    })
  })
  info(s"[graph metadata] ${gtx.metadata}")

  info(s"[requests] $requests")

  it should "compute standard" in {
    val standardResponse =
      requests.map(standardEvaluator.eval).flatMap(_.applicable).toSet
    val graphResponse =
      requests.map(graphEvaluator.eval).flatMap(_.applicable).toSet

    info(standardResponse.toString)
    info(graphResponse.toString)

    standardResponse shouldEqual graphResponse
  }

}
