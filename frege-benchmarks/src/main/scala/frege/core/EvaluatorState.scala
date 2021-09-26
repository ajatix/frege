package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import ninja.scala.frege.Request
import ninja.scala.frege.core.engine._
import org.openjdk.jmh.annotations.{Scope, State}

@State(Scope.Benchmark)
class EvaluatorState {

  val ruleGenerator = new RuleGenerator(0.3)
  val (rules, negativeRules) = ruleGenerator.generate(10)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  gtx.graph.forEach((feature, dimensions) => {
    println(feature)
    dimensions.forEach((dimension, ruleResult) => {
      println(dimension, ruleResult)
    })
  })
  println(gtx.metadata)

  val requestGenerator = new RequestGenerator()
  val requests: Seq[Request] = requestGenerator.generate(10)
}
