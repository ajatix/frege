package frege.core

object EvaluatorLoop extends App {

  while (true) {
    val state = new EvaluatorState_1000_1000
    state.requests.map(state.graphEvaluator.eval)
  }

}
