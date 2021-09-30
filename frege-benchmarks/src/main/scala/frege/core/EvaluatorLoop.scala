package frege.core

object EvaluatorLoop extends App {

  while (true) {
    val state = new EvaluatorState()
    state.requests.map(state.graphEvaluator.eval)
  }

}
