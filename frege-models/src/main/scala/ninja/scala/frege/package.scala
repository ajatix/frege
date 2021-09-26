package ninja.scala

package object frege {

  type Id = Int
  type Name = String

  trait HasId {
    def id: Id
  }

  trait HasName {
    def name: Name
  }

}
