package ninja.scala.frege.core.engine

import it.unimi.dsi.fastutil.booleans.BooleanArrays
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectOpenHashMap
}
import ninja.scala.frege.{Id, SimpleRule}

import java.util.function.IntConsumer

class RuleResult {
  protected val positive: Object2ObjectMap[Id, (Int, Int)] =
    new Object2ObjectOpenHashMap[Id, (Int, Int)]()
  protected val negative: Object2ObjectMap[Id, (Int, Int)] =
    new Object2ObjectOpenHashMap[Id, (Int, Int)]()

  /*
  0 0 0 1 1
  0 0 1 0 2
  0 1 0 0 4
  1 0 0 0 8
  1 1 1 1
   */
  def add(that: RuleResult): Unit = {
    that.positive.forEach { case (id, (newIdx, target)) =>
      if (newIdx != target) {
        if (this.positive.containsKey(id)) {
          val (seenIdx, _) = this.positive.get(id)
          addPositive(id, newIdx | seenIdx, target)
        } else addPositive(id, newIdx, target)
      }
    }
    that.negative.forEach { case (id, (newIdx, target)) =>
      if (newIdx != target) {
        if (this.negative.containsKey(id)) {
          val (seenIdx, _) = this.negative.get(id)
          addNegative(id, newIdx | seenIdx, target)
        } else addNegative(id, newIdx, target)
      }
    }
  }

  def addPositive(id: Id, index: Int = 0, target: Int = 1): Unit = {
    positive.put(id, (index, target))
  }

  def addNegative(id: Id, index: Int = 0, target: Int = 1): Unit = {
    negative.put(id, (index, target))
  }

  def getPositive: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    positive.forEach { case (id, (seen, target)) =>
      if (seen == target) set.add(id)
    }
    set
  }

  def getNegative: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    negative.forEach { case (id, (seen, target)) =>
      if (seen == target) set.add(id)
    }
    set
  }

  def getApplicable(implicit ctx: EvaluationContext): Set[Int] = {
    val builder = Set.newBuilder[Int]
    val positive = getPositive
    val negative = getNegative
    val consumer: IntConsumer = id =>
      ctx.rules.foreach { case (id, rule: SimpleRule) =>
        if (!rule.negative.exists(neg => negative.contains(neg.id)))
          builder += id
      }
    positive.forEach(consumer)
    builder.result()
  }

  override def toString: String = s"positive: $positive, negative: $negative"

}
