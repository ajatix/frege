package frege

final case class Segment(id: Id, feature: Feature, fences: Set[Fence])
    extends HasId {
  def eval(req: Field): Boolean =
    fences.exists(_.contains(req))

  override def toString: String =
    s"if ${feature.name} is ${fences.mkString(",")}"
}
