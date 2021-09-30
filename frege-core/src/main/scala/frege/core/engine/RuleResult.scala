package frege.core.engine

import frege.Id
import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectMaps,
  Object2ObjectOpenHashMap
}

import java.util.function.Consumer
import scala.collection.mutable

class RuleResult {
  protected val positive: Object2ObjectMap[Id, Result] =
    new Object2ObjectOpenHashMap[Id, Result]()
  protected val negative: Object2ObjectMap[Id, Result] =
    new Object2ObjectOpenHashMap[Id, Result]()

  def all(): Set[Int] = {
    val pos = Set.newBuilder[Int]
    val neg = Set.newBuilder[Int]
    positive.keySet().forEach { id =>
      pos += id
    }
    negative.keySet().forEach { id =>
      neg += id
    }
    pos.result() diff neg.result()
  }

  /*
  0 0 0 1 1
  0 0 1 0 2
  0 1 0 0 4
  1 0 0 0 8
  1 1 1 1
   */
  def add(that: RuleResult): Unit = {
    val postiveConsumer: Consumer[Object2ObjectMap.Entry[Id, Result]] = entry =>
      {
        val id = entry.getKey
        val thatResult = entry.getValue
        if (this.positive.containsKey(id)) {
          val thisResult = this.positive.get(id)
          addPositive(id, thisResult add thatResult)
        } else addPositive(id, thatResult)
      }
    Object2ObjectMaps.fastForEach(that.positive, postiveConsumer)
    val negativeConsumer: Consumer[Object2ObjectMap.Entry[Id, Result]] =
      entry => {
        val id = entry.getKey
        val thatResult = entry.getValue
        if (this.negative.containsKey(id)) {
          val thisResult = this.negative.get(id)
          addNegative(id, thisResult add thatResult)
        } else addNegative(id, thatResult)
      }
    Object2ObjectMaps.fastForEach(that.negative, negativeConsumer)
  }

  def addPositive(id: Id, result: Result): Unit = {
    positive.put(id, result)
  }

  def addNegative(id: Id, result: Result): Unit = {
    negative.put(id, result)
  }

  def getApplicable(implicit gtx: GraphEvaluationContext): EvaluationResult = {
    val applicable: mutable.Builder[Id, Set[Id]] = Set.newBuilder[Id]
    val consumer: Consumer[Object2ObjectMap.Entry[Id, Result]] = entry => {
      val posId = entry.getKey
      val positiveResult = entry.getValue
      if (positiveResult.isPass) {
        val it = gtx.negativeRuleMap(posId).iterator()
        var continue: Boolean = true
        while (continue && it.hasNext) {
          val next = it.nextInt()
          if (negative.containsKey(next)) {
            if (negative.get(next).isPass) continue = false
          }
        }
        if (continue) applicable += posId
      }
    }
    Object2ObjectMaps.fastForEach(positive, consumer)
    EvaluationResult(applicable.result())
  }

  override def toString: String = s"positive: $positive, negative: $negative"

}
