package frege

final case class Fence(v: Field) {
  override def toString: String = s"${v.v}"
}
