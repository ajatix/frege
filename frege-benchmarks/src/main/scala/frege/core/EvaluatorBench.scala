package frege.core

import org.openjdk.jmh.annotations.{
  Benchmark,
  BenchmarkMode,
  Mode,
  OutputTimeUnit
}

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class EvaluatorBench {

  @Benchmark
  def standardEvaluator(state: EvaluatorState): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  def graphEvaluator(state: EvaluatorState): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

}
