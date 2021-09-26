package ninja.scala.frege.core

import ninja.scala.frege._
import ninja.scala.frege.core.engine._
import ninja.scala.frege.syntax._

case class SimpleRequest(
    origin: String,
    payment: Int,
    loggedIn: Boolean,
    traffic: String
) extends Request {
  override def get(feature: String): Option[Field] = feature match {
    case "origin"    => Some(origin)
    case "payment"   => Some(payment)
    case "logged_in" => Some(loggedIn)
    case "traffic"   => Some(traffic)
  }
}

object Context {

  val negativeRules = Map(
    1 ->
      SimpleRule(
        1,
        "Block_Payment",
        action = Skip,
        positive = Set(
          Segment(
            1,
            Feature(1, "payment"),
            Set(
              Fence(1)
            )
          )
        )
      )
  )
  val rules = Map(
    1 -> SimpleRule(
      1,
      "FREGE-1",
      Lift(5.0),
      positive = Set(
        Segment(1, feature = Feature(1, "origin"), fences = Set(Fence("TH")))
      ),
      negative = Set(negativeRules(1))
    )
  )
}

object Main extends App {

  implicit val ctx: EvaluationContext = EvaluationContext(Context.rules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  def runner(request: SimpleRequest): Unit = {
    println("request")
    println(request)

    println("standard response")
    println(standardEvaluator.eval(request))

    println("graph response")
    println(graphEvaluator.eval(request))
  }

  runner(
    SimpleRequest(
      origin = "TH",
      payment = 2,
      loggedIn = true,
      traffic = "direct"
    )
  )

  runner(
    SimpleRequest(
      origin = "TH",
      payment = 1,
      loggedIn = true,
      traffic = "direct"
    )
  )

}
