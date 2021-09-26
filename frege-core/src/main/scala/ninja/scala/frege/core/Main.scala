package ninja.scala.frege.core

import ninja.scala.frege.syntax._
import ninja.scala.frege._

import scala.collection.mutable

class RuleMeta {
  private val positive: mutable.HashSet[Int] = mutable.HashSet.empty
  private val negative: mutable.HashSet[Int] = mutable.HashSet.empty

  def addPositive(id: Id): Unit = {
    positive.add(id)
  }

  def addNegative(id: Id): Unit = {
    negative.add(id)
  }

  def getPositive: Set[Int] = positive.toSet

  def getNegative: Set[Int] = negative.toSet

  override def toString: Name = s"positive: $positive, negative: $negative"
}

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

object Main extends App {

  println("rules")
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
  println(rules)

  val graph = mutable.HashMap.empty[String, mutable.HashMap[Field, RuleMeta]]
  rules.foreach { case (ruleId, SimpleRule(_, _, _, positive, negative)) =>
    positive.foreach { case Segment(_, feature, fences) =>
      if (!graph.contains(feature.name))
        graph.put(feature.name, mutable.HashMap.empty[Field, RuleMeta])
      graph.get(feature.name) match {
        case Some(featureGraph) =>
          fences.foreach { fence =>
            if (!featureGraph.contains(fence.v))
              featureGraph.put(fence.v, new RuleMeta)
            featureGraph.get(fence.v) match {
              case Some(ruleMeta) => ruleMeta.addPositive(ruleId)
            }
          }
      }
    }
    negative.foreach { case SimpleRule(_, _, _, positive, _) =>
      positive.foreach { case Segment(_, feature, fences) =>
        if (!graph.contains(feature.name))
          graph.put(feature.name, mutable.HashMap.empty[Field, RuleMeta])
        graph.get(feature.name) match {
          case Some(featureGraph) =>
            fences.foreach { fence =>
              if (!featureGraph.contains(fence.v))
                featureGraph.put(fence.v, new RuleMeta)
              featureGraph.get(fence.v) match {
                case Some(ruleMeta) => ruleMeta.addNegative(ruleId)
              }
            }
        }
      }
    }
  }
  println(graph)

  def runner(request: SimpleRequest): Unit = {
    println("request")
    println(request)

    println("response")
    val response = rules.mapValues(_.eval(request))
    println(response)

    println("graph response")
    val graphEvaluation = graph
      .flatMap { case (feature, dimensions) =>
        request.get(feature) match {
          case Some(field) => dimensions.get(field)
        }
      }
      .reduce[RuleMeta] { case (x: RuleMeta, y: RuleMeta) =>
        x.getPositive.foreach(y.addPositive)
        x.getNegative.foreach(y.addNegative)
        y
      }
    val graphResponse = graphEvaluation.getPositive
      .map(id => id -> !graphEvaluation.getNegative.contains(id))
      .toMap
    println(graphResponse)
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
