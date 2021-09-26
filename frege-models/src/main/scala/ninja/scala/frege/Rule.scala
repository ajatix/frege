package ninja.scala.frege

sealed trait Rule extends HasId {
  def action: Action
  def applicable: Set[Segment]

  def eval(req: Request): Boolean
}

final case class SimpleRule(
    id: Id,
    action: Action,
    positive: Set[Segment],
    negative: Set[Rule] = Set.empty
) extends Rule {
  override val applicable: Set[Segment] = positive

  private def evalPositive(req: Request): Boolean = {
    positive.forall { segment =>
      req.get(segment.feature.name).exists(field => segment.eval(field))
    }
  }

  private def evalNegative(req: Request): Boolean =
    negative.exists(rule => rule.eval(req))

  override def eval(req: Request): Boolean =
    !evalNegative(req) && evalPositive(req)
}
