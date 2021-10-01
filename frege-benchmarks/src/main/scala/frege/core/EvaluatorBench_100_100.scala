package frege.core

import org.openjdk.jmh.annotations.{
  Benchmark,
  BenchmarkMode,
  Mode,
  OutputTimeUnit
}

import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MICROSECONDS)
class EvaluatorBench_100_100 {

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def standardEvaluatorSingle_100_100(state: EvaluatorState_100_100): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.SingleShotTime))
  def graphEvaluatorSingle_100_100(state: EvaluatorState_100_100): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def standardEvaluator_100_100(state: EvaluatorState_100_100): Unit = {
    state.requests.map(state.standardEvaluator.eval)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
  def graphEvaluator_100_100(state: EvaluatorState_100_100): Unit = {
    state.requests.map(state.graphEvaluator.eval)
  }

}
