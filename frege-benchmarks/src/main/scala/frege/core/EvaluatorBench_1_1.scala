package frege.core

import org.openjdk.jmh.annotations.{
  Benchmark,
  BenchmarkMode,
  Mode,
  OutputTimeUnit
}

import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MICROSECONDS)
class EvaluatorBench_1_1 {

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def standardEvaluatorSingle_1_1(state: EvaluatorState_1_1): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def graphEvaluatorSingle_1_1(state: EvaluatorState_1_1): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def standardEvaluator_1_1(state: EvaluatorState_1_1): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def graphEvaluator_1_1(state: EvaluatorState_1_1): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

}
