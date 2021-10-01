package frege.core

import org.openjdk.jmh.annotations.{
  Benchmark,
  BenchmarkMode,
  Mode,
  OutputTimeUnit
}

import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MICROSECONDS)
class EvaluatorBench_1000_1000 {

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def standardEvaluatorSingle_1000_1000(
      state: EvaluatorState_1000_1000
  ): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def graphEvaluatorSingle_1000_1000(state: EvaluatorState_1000_1000): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def standardEvaluator_1000_1000(state: EvaluatorState_1000_1000): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def graphEvaluator_1000_1000(state: EvaluatorState_1000_1000): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

}
