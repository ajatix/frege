# frege

For testing out rule engine implementations

> Who is [frege](https://en.wikipedia.org/wiki/Gottlob_Frege)? <br/> 
> Also read this [book](https://en.wikipedia.org/wiki/Logicomix) this weekend if you haven't already

### adding a custom evaluator implementation
- In [Evaluator.scala](frege-core/src/main/scala/frege/core/engine/Evaluator.scala), add your new evaluator
```scala
sealed trait Evaluator {
  def eval(request: Request): EvaluationResult
}
```
- Start with [EvaluationContext.scala](frege-core/src/main/scala/frege/core/engine/EvaluationContext.scala) which defines rules that need to filtered on a given request
- See [models](frege-models/src/main/scala/frege) for the minimum data classes required to define rules

### validating your evaluator
Add your new validator to [EvaluatorSpec.scala](frege-core/src/test/scala/frege/core/EvaluatorSpec.scala)
> The project uses scalacheck to generate random rules and requests based on a static list of features defined at [generators](frege-testkit/src/main/scala/frege/generators)

### running benchmarks
Add your new validator to [benchmarks](frege-benchmarks/src/main/scala/frege/core)
```
sbt clean benchmarks/"jmh:run -i 1 -wi 1 -f1 -t1 .*EvaluatorBench.*"
```
To see the current [benchmark results](BENCHMARKS.md)
