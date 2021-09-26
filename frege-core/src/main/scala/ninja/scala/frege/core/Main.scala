package ninja.scala.frege.core

import ninja.scala.frege._
import ninja.scala.frege.core.engine._
import ninja.scala.frege.syntax._

case class SimpleRequest(
    origin: String,
    payment: Int,
    loggedIn: Boolean,
    traffic: String,
    cid: Int
) extends Request {
  override def get(feature: String): Option[Field] = feature match {
    case "origin"    => Some(origin)
    case "payment"   => Some(payment)
    case "logged_in" => Some(loggedIn)
    case "traffic"   => Some(traffic)
    case "cid"       => Some(cid)
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
      ),
    2 ->
      SimpleRule(
        2,
        "Block_Guest",
        action = Skip,
        positive = Set(
          Segment(
            1,
            Feature(1, "logged_in"),
            Set(
              Fence(false)
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
        Segment(
          1,
          feature = Feature(1, "logged_in"),
          fences = Set(Fence(true))
        ),
        Segment(
          1,
          feature = Feature(1, "traffic"),
          fences = Set(Fence("direct"))
        )
      ),
      negative = Set(negativeRules(1))
    ),
    2 -> SimpleRule(
      2,
      "FREGE-2",
      Lift(1.0),
      positive = Set(
        Segment(1, feature = Feature(1, "origin"), fences = Set(Fence("TH"))),
        Segment(
          1,
          feature = Feature(1, "logged_in"),
          fences = Set(Fence(true))
        ),
        Segment(
          1,
          feature = Feature(1, "traffic"),
          fences = Set(Fence("direct"))
        ),
        Segment(1, feature = Feature(1, "payment"), fences = Set(Fence(2)))
      )
    ),
    3 -> SimpleRule(
      3,
      "FREGE-3",
      Lift(0.5),
      positive = Set(
        Segment(
          1,
          feature = Feature(1, "logged_in"),
          fences = Set(Fence(false))
        ),
        Segment(
          1,
          feature = Feature(1, "traffic"),
          fences = Set(Fence("direct"))
        )
      )
    ),
    4 -> SimpleRule(
      4,
      "FREGE-4",
      Lift(2.0),
      positive = Set(
        Segment(1, feature = Feature(1, "cid"), fences = Set(Fence(1))),
        Segment(1, feature = Feature(1, "traffic"), fences = Set(Fence("mse")))
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

  println("graph metadata")
  println(gtx.metadata)

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
      traffic = "direct",
      cid = 1
    )
  )

  runner(
    SimpleRequest(
      origin = "TH",
      payment = 1,
      loggedIn = true,
      traffic = "direct",
      cid = 2
    )
  )

}
