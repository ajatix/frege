package ninja.scala.frege.core.engine

import ninja.scala.frege.Id

import scala.collection.mutable

class RuleResult {
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

  override def toString: String = s"positive: $positive, negative: $negative"
}
