package frege

trait Request {
  def get(feature: String): Option[Field]
}
