package frege.generators

import ninja.scala.frege._
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

class RuleGenerator(
    fraction: Double,
    params: Gen.Parameters = Gen.Parameters.default,
    seed: Seed = Seed.random()
) extends Generator[(Map[Id, Rule], Map[Id, Rule])] {

  override def generate(num: Id): (Map[Id, Rule], Map[Id, Rule]) = {
    val split: Int = (num * fraction).toInt
    val negative = (1 until split)
      .map(idx => RuleGenerator.negativeGenerator(idx).pureApply(params, seed))
      .toSet
    val positive = (split to num).map(idx =>
      RuleGenerator.positiveGenerator(idx, negative).pureApply(params, seed)
    )
    (positive.map(r => r.id -> r).toMap, negative.map(r => r.id -> r).toMap)
  }
}

object RuleGenerator {
  def genName(id: Id, prefix: String): Gen[String] = Gen.const(s"$prefix-$id")
  def genFence(feature: String): Gen[Fence] = for {
    request <- RequestGenerator.generator
    field <- Gen.oneOf(request.get(feature))
  } yield Fence(field)
  def genSegment(id: Id): Gen[Segment] = for {
    feature <- Gen.oneOf("traffic", "origin", "payment", "cid", "logged_in")
    fences <- Gen.nonEmptyListOf(genFence(feature)).map(_.toSet)
  } yield Segment(id = id, feature = Feature(id, feature), fences = fences)
  def negativeGenerator(id: Id): Gen[Rule] = for {
    name <- genName(id, "block")
    positive <- Gen.nonEmptyListOf(genSegment(id)).map(_.toSet)
  } yield SimpleRule(id = id, name = name, action = Skip, positive = positive)
  def positiveGenerator(id: Id, negative: Set[Rule]): Gen[Rule] = for {
    name <- genName(id, "block")
    action <- Gen.choose(-10.0, 10.0)
    positive <- Gen.nonEmptyListOf(genSegment(id)).map(_.toSet)
    negative <- Gen.someOf(negative).map(_.toSet)
  } yield SimpleRule(
    id = id,
    name = name,
    action = Lift(action),
    positive = positive,
    negative = negative
  )
}
