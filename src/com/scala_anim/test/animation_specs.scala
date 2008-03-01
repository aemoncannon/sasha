package com.scala_anim.test
import com.scala_anim._
import com.scala_anim.animation._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.event._

object animationSpecs extends Specification {

  "A frame animation" should {
    "tick on every ENTER_FRAME event." in {
      val s = Stage.stage
      val c1 = new Sprite()
      s.addChild(c1)
      Anim.doWhile{ c1.x += 1 }{ c1.x < 10 }
      for (i <- (0 to 20)){ 
	s.propagateEventCaptureOnly(new PropagatingEvent(PropagatingEvent.ENTER_FRAME))
      }
      c1.x must_== 10
    }
  }

  "A frame animation" should {
    "fire an UPDATE event on every tick." in {
      val s = Stage.stage
      val c1 = new Sprite()
      s.addChild(c1)
      c1.x must_== 0.0
      var counter:Float = 0.0f
      val anim = Anim.doWhile{ c1.x += 1 }{ c1.x < 10 }
      anim.addEventListenerInline(Anim.FINISHED){ e:Event => 
	counter += 1 
      }
      for (i <- (0 to 11)){
	s.propagateEventCaptureOnly(new PropagatingEvent(PropagatingEvent.ENTER_FRAME))
      }
      counter must_== 1
    }
  }

}
