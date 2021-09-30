package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import ninja.scala.frege.Request
import ninja.scala.frege.core.engine._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EvaluatorSpec extends AnyFlatSpec with Matchers {

  behavior of "evaluator"

  val requestGenerator = new RequestGenerator()
  val ruleGenerator = new RuleGenerator(0.3)

  val (rules, negativeRules) = ruleGenerator.generate(100)
  val requests: Seq[Request] = requestGenerator.generate(10)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  it should "compute standard" in {
    val standardResponse =
      requests
        .map(request => request -> standardEvaluator.eval(request).applicable)
        .toMap
    val graphResponse =
      requests
        .map(request => request -> graphEvaluator.eval(request).applicable)
        .toMap

    graphResponse shouldEqual standardResponse
  }

}
