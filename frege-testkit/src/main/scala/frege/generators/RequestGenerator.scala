package frege.generators

import ninja.scala.frege.syntax._
import ninja.scala.frege.{Field, Request}
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

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

class RequestGenerator(
    params: Gen.Parameters = RequestGenerator.defaultParams,
    seed: Seed = RequestGenerator.defaultSeed
) extends Generator[Seq[Request]] {

  def generate(num: Int): Seq[Request] = {
    Gen.listOfN(num, RequestGenerator.generator).pureApply(params, seed)
  }

}

object RequestGenerator {

  val defaultParams: Gen.Parameters = Gen.Parameters.default.withSize(5)
  val defaultSeed: Seed = Seed(42L)

  val generator: Gen[Request] = for {
    origin <- Gen.oneOf("TH", "IN", "US")
    payment <- Gen.oneOf(1, 2, 3)
    loggedIn <- Gen.oneOf(true, false)
    traffic <- Gen.oneOf("direct", "mse")
    cid <- Gen.oneOf(1, 2, 3, 4, 5)
  } yield SimpleRequest(
    origin = origin,
    payment = payment,
    loggedIn = loggedIn,
    traffic = traffic,
    cid = cid
  )
}
