package frege

sealed trait Field {
  type T
  def v: T

  override def toString: String = v.toString
}

final case class StringField(v: String) extends Field {
  type T = String
}

final case class IntField(v: Int) extends Field {
  type T = Int
}

final case class BooleanField(v: Boolean) extends Field {
  type T = Boolean
}
