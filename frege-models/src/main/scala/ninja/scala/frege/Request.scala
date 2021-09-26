package ninja.scala.frege

trait Request {

  def get(feature: String): Option[Field]

}
