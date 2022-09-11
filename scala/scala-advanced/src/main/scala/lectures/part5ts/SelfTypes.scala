package lectures.part5ts

object SelfTypes extends App {
  trait Instrumenalist {
    def play(): Unit
  }
  trait Singer { self: Instrumenalist =>
    def sing(): Unit
  }
  class LeadSinger extends Singer with Instrumenalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }
  val jamesHetfield = new Singer with Instrumenalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }
  class Guitarisit extends Instrumenalist {
    override def play(): Unit = println("guitar solo")
  }
  val ericClaptor = new Guitarisit with Singer {
    override def sing(): Unit = ???
  }
  class A
  class B extends A

  trait T
  trait S { self : T => }

  class Component {

  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  trait ScalaComponent {
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + "This rocks"
  }

  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats



}
