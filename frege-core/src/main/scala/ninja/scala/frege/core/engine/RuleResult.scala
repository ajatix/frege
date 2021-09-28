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
  protected val positive: Object2ObjectMap[Id, Array[Boolean]] =
    new Object2ObjectOpenHashMap[Id, Array[Boolean]]()
  protected val negative: Object2ObjectMap[Id, Array[Boolean]] =
    new Object2ObjectOpenHashMap[Id, Array[Boolean]]()

  /*
  0 0 0 1 1
  0 0 1 0 2
  0 1 0 0 4
  1 0 0 0 8
  1 1 1 1
   */
  def add(that: RuleResult): Unit = {
    that.positive.forEach { (id, bits) =>
      if (bits.nonEmpty) {
        val accBits = this.positive.getOrDefault(id, Array.empty)
        if (accBits.nonEmpty)
          addPositive(
            id,
            accBits.zip(bits).map { case (x, y) =>
              x || y
            }
          )
        else addPositive(id, bits)
      }
    }
    that.negative.forEach { (id, bits) =>
      if (bits.nonEmpty) {
        val accBits = this.negative.getOrDefault(id, Array.empty)
        if (accBits.nonEmpty)
          addNegative(
            id,
            accBits.zip(bits).map { case (x, y) =>
              x || y
            }
          )
        else addNegative(id, bits)
      }
    }
  }

  def addPositive(id: Id, index: Int = 0, target: Int = 1): Unit = {
    val bits = BooleanArrays.ensureCapacity(BooleanArrays.EMPTY_ARRAY, target)
    bits(index) = true
    addPositive(id, bits)
  }

  def addPositive(id: Id, bits: Array[Boolean]): Unit = {
    positive.put(id, bits)
  }

  def addNegative(id: Id, index: Int = 0, target: Int = 1): Unit = {
    val bits = BooleanArrays.ensureCapacity(BooleanArrays.EMPTY_ARRAY, target)
    bits(index) = true
    addNegative(id, bits)
  }

  def addNegative(id: Id, bits: Array[Boolean]): Unit = {
    negative.put(id, bits)
  }

  def getPositive: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    positive.forEach { (id, bits) =>
      if (!bits.contains(false)) set.add(id)
    }
    set
  }

  def getNegative: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    negative.forEach { (id, bits) =>
      if (!bits.contains(false)) set.add(id)
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
