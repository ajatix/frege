package frege.generators

trait Generator[T] {

  def generate(num: Int): T

}
