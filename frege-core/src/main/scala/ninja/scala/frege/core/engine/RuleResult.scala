package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

import scala.collection.mutable

class RuleResult {
  protected val positive: mutable.HashMap[Id, Float] = mutable.HashMap.empty
  protected val negative: mutable.HashMap[Id, Float] = mutable.HashMap.empty

  def add(that: RuleResult): Unit = {
    that.positive.foreach { case (id, weight) =>
      addPositive(id, weight)
    }
    that.negative.foreach { case (id, weight) =>
      addNegative(id, weight)
    }
  }

  def addPositive(id: Id, weight: Float = 1.0f): Unit = {
    val count = positive.getOrElseUpdate(id, 0.0f)
    positive.update(id, count + weight)
  }

  def addNegative(id: Id, weight: Float = 1.0f): Unit = {
    val count = negative.getOrElseUpdate(id, 0.0f)
    negative.update(id, count + weight)
  }

  def getPositive: Set[Int] = positive
    .filter { case (_, weight) => weight >= 0.99 }
    .keys
    .toSet

  def getNegative: Set[Int] = negative
    .filter { case (_, weight) => weight >= 0.99 }
    .keys
    .toSet

  def getApplicable: Set[Int] = {
    getPositive.diff(getNegative)
  }

  override def toString: String = s"positive: $positive, negative: $negative"

}
