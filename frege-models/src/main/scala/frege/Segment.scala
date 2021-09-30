package frege

final case class Segment(id: Id, feature: Feature, fences: Set[Fence])
    extends HasId {
  def eval[T <: Field](req: T)(implicit ev: Predicate[T]): Boolean =
    fences.exists(ev.eval(req, _))
}
