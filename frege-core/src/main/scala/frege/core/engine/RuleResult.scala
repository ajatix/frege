package frege.core.engine

import it.unimi.dsi.fastutil.objects.{
  Object2ObjectMap,
  Object2ObjectOpenHashMap
}
import frege.Id

import scala.collection.mutable

class RuleResult {
  protected val positive: Object2ObjectMap[Id, Result] =
    new Object2ObjectOpenHashMap[Id, Result]()
  protected val negative: Object2ObjectMap[Id, Result] =
    new Object2ObjectOpenHashMap[Id, Result]()

  /*
  0 0 0 1 1
  0 0 1 0 2
  0 1 0 0 4
  1 0 0 0 8
  1 1 1 1
   */
  def add(that: RuleResult): Unit = {
    that.positive.forEach { case (id, thatResult) =>
      if (this.positive.containsKey(id)) {
        val thisResult = this.positive.get(id)
        addPositive(id, thisResult add thatResult)
      } else addPositive(id, thatResult)
    }
    that.negative.forEach { case (id, thatResult) =>
      if (this.negative.containsKey(id)) {
        val thisResult = this.negative.get(id)
        addNegative(id, thisResult add thatResult)
      } else addNegative(id, thatResult)
    }
  }

  def addPositive(id: Id, result: Result): Unit = {
    positive.put(id, result)
  }

  def addNegative(id: Id, result: Result): Unit = {
    negative.put(id, result)
  }

  def getApplicable(implicit gtx: GraphEvaluationContext): EvaluationResult = {
    val applicable: mutable.Builder[Id, Set[Id]] = Set.newBuilder[Id]
    positive.forEach { case (posId, positiveResult) =>
      val blockedBy = gtx.negativeRuleMap
        .get(posId)
        .collect {
          case negId if negative.containsKey(negId) =>
            negative.get(negId)
        }
        .reduceOption(Result.semigroup.combine)
      if (!blockedBy.exists(_.isFail) && positiveResult.isPass)
        applicable += posId
    }
    EvaluationResult(applicable.result())
  }

  override def toString: String = s"positive: $positive, negative: $negative"

}
