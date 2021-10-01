package frege.core

import frege.generators.{RequestGenerator, RuleGenerator}
import frege.Request
import frege.core.engine._
import org.openjdk.jmh.annotations.{Scope, State}

abstract class EvaluatorState(numRules: Int, numRequests: Int) {

  val ruleGenerator = new RuleGenerator(0.3)
  val (rules, negativeRules) = ruleGenerator.generate(numRules)

  implicit val ctx: EvaluationContext =
    EvaluationContext(rules, negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  val requestGenerator = new RequestGenerator()
  val requests: Seq[Request] = requestGenerator.generate(numRequests)
}

@State(Scope.Benchmark)
class EvaluatorState_1_1 extends EvaluatorState(1, 1)

@State(Scope.Benchmark)
class EvaluatorState_10_1 extends EvaluatorState(10, 1)

@State(Scope.Benchmark)
class EvaluatorState_10_10 extends EvaluatorState(10, 10)

@State(Scope.Benchmark)
class EvaluatorState_100_1 extends EvaluatorState(100, 1)

@State(Scope.Benchmark)
class EvaluatorState_100_10 extends EvaluatorState(100, 10)

@State(Scope.Benchmark)
class EvaluatorState_100_100 extends EvaluatorState(100, 100)

@State(Scope.Benchmark)
class EvaluatorState_1000_1 extends EvaluatorState(1000, 1)

@State(Scope.Benchmark)
class EvaluatorState_1000_10 extends EvaluatorState(1000, 10)

@State(Scope.Benchmark)
class EvaluatorState_1000_100 extends EvaluatorState(1000, 100)

@State(Scope.Benchmark)
class EvaluatorState_1000_1000 extends EvaluatorState(1000, 1000)
