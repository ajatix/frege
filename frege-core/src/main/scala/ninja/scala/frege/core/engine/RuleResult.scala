package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

import scala.collection.mutable

class RuleResult {
  private val positive: mutable.HashMap[Id, Int] = mutable.HashMap.empty
  private val negative: mutable.HashMap[Id, Int] = mutable.HashMap.empty

  def addPositive(id: Id): Unit = {
    val count = positive.getOrElseUpdate(id, 0)
    positive.update(id, count + 1)
  }

  def addNegative(id: Id): Unit = {
    val count = negative.getOrElseUpdate(id, 0)
    negative.update(id, count + 1)
  }

  def getPositive: Set[Int] = positive.keys.toSet

  def filterPositive(rules: Map[Id, Int]): Set[Int] = positive
    .filter { case (id, target) =>
      rules.get(id).contains(target)
    }
    .keys
    .toSet

  def getNegative: Set[Int] = negative.keys.toSet

  def filterNegative(rules: Map[Id, Int]): Set[Int] = negative
    .filter { case (id, target) =>
      rules.get(id).contains(target)
    }
    .keys
    .toSet

  override def toString: String = s"positive: $positive, negative: $negative"
}
