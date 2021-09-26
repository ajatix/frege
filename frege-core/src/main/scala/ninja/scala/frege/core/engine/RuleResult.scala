package ninja.scala.frege.core.engine

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectOpenHashMap
}
import ninja.scala.frege.Id

import java.util.function.IntConsumer

class RuleResult {
  protected val positive: Object2ObjectMap[Id, Float] =
    new Object2ObjectOpenHashMap[Id, Float]()
  protected val negative: Object2ObjectMap[Id, Object2ObjectMap[Id, Float]] =
    new Object2ObjectOpenHashMap[Id, Object2ObjectMap[Id, Float]]()

  def add(that: RuleResult): Unit = {
    that.positive.forEach { (id, weight) =>
      addPositive(id, weight)
    }
    that.negative.forEach { (id, ruleMap) =>
      ruleMap.forEach { (ruleId, weight) =>
        addNegative(id, ruleId, weight)
      }
    }
  }

  def addPositive(id: Id, weight: Float = 1.0f): Unit = {
    val count = positive.getOrDefault(id, 0.0f)
    positive.put(id, count + weight)
  }

  def addNegative(id: Id, ruleId: Id, weight: Float = 1.0f): Unit = {
    val ruleMap =
      negative.getOrDefault(id, new Object2ObjectOpenHashMap[Id, Float]())
    ruleMap.put(ruleId, weight)
    negative.put(id, ruleMap)
  }

  def getPositive: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    positive.forEach { (id, weight) =>
      if (weight >= 0.99) set.add(id)
    }
    set
  }

  def getNegative: IntOpenHashSet = {
    val set = new IntOpenHashSet()
    negative.forEach { (_, counts) =>
      counts.forEach { (ruleId, weight) =>
        if (weight >= 0.99) set.add(ruleId)
      }
    }
    set
  }

  def getApplicable: Set[Int] = {
    val builder = Set.newBuilder[Int]
    val positive = getPositive
    val negative = getNegative
    val consumer: IntConsumer = id => if (!negative.contains(id)) builder += id
    positive.forEach(consumer)
    builder.result()
  }

  override def toString: String = s"positive: $positive, negative: $negative"

}
