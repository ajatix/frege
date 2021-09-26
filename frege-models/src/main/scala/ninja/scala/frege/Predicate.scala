package ninja.scala.frege

trait Predicate[T <: Field] {
  def eval(req: T, fence: Fence): Boolean
}

object Predicate {

  implicit val stringPredicate: Predicate[StringField] =
    (req: StringField, fence: Fence) =>
      fence.v match {
        case StringField(v) => req.v == v
        case _              => false
      }
  implicit val intPredicate: Predicate[IntField] =
    (req: IntField, fence: Fence) =>
      fence.v match {
        case IntField(v) => req.v == v
        case _           => false
      }
  implicit val booleanPredicate: Predicate[BooleanField] =
    (req: BooleanField, fence: Fence) =>
      fence.v match {
        case BooleanField(v) => req.v == v
        case _               => false
      }

  implicit val predicate: Predicate[Field] = new Predicate[Field] {
    override def eval(req: Field, fence: Fence): Boolean = req match {
      case r: StringField  => stringPredicate.eval(r, fence)
      case r: IntField     => intPredicate.eval(r, fence)
      case r: BooleanField => booleanPredicate.eval(r, fence)
    }
  }

}
