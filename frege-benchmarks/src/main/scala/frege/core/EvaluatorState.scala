package frege.core

import ninja.scala.frege.core.engine.{
  EvaluationContext,
  GraphEvaluationContext,
  GraphEvaluationContextBuilder,
  GraphEvaluator,
  StandardEvaluator
}
import ninja.scala.frege.{
  Feature,
  Fence,
  Field,
  Lift,
  Request,
  Segment,
  SimpleRule,
  Skip
}
import org.openjdk.jmh.annotations.{Scope, State}
import ninja.scala.frege.syntax._

@State(Scope.Benchmark)
class EvaluatorState {

  implicit val ctx: EvaluationContext =
    EvaluationContext(Context.rules, Context.negativeRules)
  implicit val gtx: GraphEvaluationContext =
    new GraphEvaluationContextBuilder().build()
  val standardEvaluator = new StandardEvaluator()
  val graphEvaluator = new GraphEvaluator()

  gtx.graph.foreach(println)
  println(gtx.metadata)

  val requests: Seq[Request] = Seq(
    SimpleRequest(
      origin = "TH",
      payment = 2,
      loggedIn = true,
      traffic = "direct",
      cid = 1
    ),
    SimpleRequest(
      origin = "TH",
      payment = 1,
      loggedIn = true,
      traffic = "direct",
      cid = 2
    )
  )

}

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
      negative = Set(negativeRules(2))
    ),
    5 -> SimpleRule(
      5,
      "FREGE-5",
      Lift(0.1),
      positive = Set(
        Segment(1, feature = Feature(1, "cid"), fences = Set(Fence(1))),
        Segment(1, feature = Feature(1, "traffic"), fences = Set(Fence("mse")))
      ),
      negative = Set(negativeRules(2))
    ),
    6 -> SimpleRule(
      6,
      "FREGE-6",
      Lift(-0.2),
      positive = Set(
        Segment(1, feature = Feature(1, "cid"), fences = Set(Fence(1))),
        Segment(1, feature = Feature(1, "traffic"), fences = Set(Fence("mse")))
      ),
      negative = Set(negativeRules(2))
    ),
    7 -> SimpleRule(
      7,
      "FREGE-7",
      Lift(1.3),
      positive = Set(
        Segment(1, feature = Feature(1, "cid"), fences = Set(Fence(1))),
        Segment(1, feature = Feature(1, "traffic"), fences = Set(Fence("mse")))
      ),
      negative = Set(negativeRules(2))
    )
  )
}