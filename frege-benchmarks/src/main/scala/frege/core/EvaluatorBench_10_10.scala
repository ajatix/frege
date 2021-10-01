package frege.core

import org.openjdk.jmh.annotations.{
  Benchmark,
  BenchmarkMode,
  Mode,
  OutputTimeUnit
}

import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MICROSECONDS)
class EvaluatorBench_10_10 {

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def standardEvaluatorSingle_10_10(state: EvaluatorState_10_10): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def graphEvaluatorSingle_10_10(state: EvaluatorState_10_10): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def standardEvaluator_10_10(state: EvaluatorState_10_10): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def graphEvaluator_10_10(state: EvaluatorState_10_10): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

}
