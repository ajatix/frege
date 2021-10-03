package frege

sealed trait Fence {
  def contains(field: Field): Boolean
}

private case object UniversalFence extends Fence {
  override def contains(field: Field): Boolean = true
  override def toString: String = "always"
}

private case class SingletonFence(v: Field, negate: Boolean = false)
    extends Fence {
  override def contains(field: Field): Boolean = v == field ^ negate
  override def toString: String = if (negate) s"neq($v)" else s"eq($v)"
}

private case class IntervalFence(
    start: Int,
    end: Int,
    lInclusive: Boolean,
    rInclusive: Boolean
) extends Fence {
  final val lContains: Int => Boolean = num =>
    (lInclusive && num == start) || num > start
  final val rContains: Int => Boolean = num =>
    (rInclusive && num == end) || num < end

  override def contains(field: Field): Boolean = field match {
    case IntField(v) => lContains(v) && rContains(v)
    case _           => false
  }

  override def toString: Name = {
    s"""${if (lInclusive) "[" else "("}${if (start == Int.MinValue) "-∞"
    else s"$start"}, ${if (end == Int.MaxValue) "+∞" else s"$end"}${if (
      rInclusive
    ) "]"
    else ")"}"""
  }
}

trait Contains[T, K] {
  def contains(iter: T, key: K): Boolean
}

private case class DiscreteFence[T](v: T)(implicit ev: Contains[T, Field])
    extends Fence {
  override def contains(field: Field): Boolean = ev.contains(v, field)
}

object Fence {
  val all: Fence = UniversalFence
  def eq(field: Field): Fence = SingletonFence(field)
  def neq(field: Field): Fence = SingletonFence(field, negate = true)
  def lt(field: IntField): Fence = IntervalFence(
    start = Int.MinValue,
    end = field.v,
    lInclusive = false,
    rInclusive = false
  )
  def lteq(field: IntField): Fence = IntervalFence(
    start = Int.MinValue,
    end = field.v,
    lInclusive = false,
    rInclusive = true
  )
  def gt(field: IntField): Fence = IntervalFence(
    start = field.v,
    end = Int.MaxValue,
    lInclusive = false,
    rInclusive = false
  )
  def gteq(field: IntField): Fence = IntervalFence(
    start = field.v,
    end = Int.MaxValue,
    lInclusive = true,
    rInclusive = false
  )
  def in(from: IntField, to: IntField): Fence = IntervalFence(
    start = from.v,
    end = to.v,
    lInclusive = false,
    rInclusive = true
  )
  def in[T](v: T)(implicit ev: Contains[T, Field]): Fence = DiscreteFence(v)
}
