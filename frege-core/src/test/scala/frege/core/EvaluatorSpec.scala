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

  val (rules, negativeRules) = ruleGenerator.generate(1)
  val requests: Seq[Request] = requestGenerator.generate(2)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  it should "compute standard" in {
    val standardResponse =
      state.requests.map(state.standardEvaluator.eval).flatMap(_.applicable)
    val graphResponse =
      state.requests.map(state.graphEvaluator.eval).flatMap(_.applicable)
    println(standardResponse)
    println(graphResponse)
    println(standardResponse == graphResponse)
    assert(standardResponse == graphResponse)
  }

}
