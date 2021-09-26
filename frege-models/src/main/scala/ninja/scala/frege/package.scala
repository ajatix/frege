package ninja.scala

import scala.language.implicitConversions

package object frege {

  type Id = Int
  type Name = String

  trait HasId {
    def id: Id
  }

  trait HasName {
    def name: Name
  }

  object syntax {
    implicit def toStringField(v: String): StringField = StringField(v)
    implicit def toIntField(v: Int): IntField = IntField(v)
    implicit def toBooleanField(v: Boolean): BooleanField = BooleanField(v)
  }

}
