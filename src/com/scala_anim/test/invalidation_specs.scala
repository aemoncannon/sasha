package com.scala_anim.test
import com.scala_anim._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.util._
import com.scala_anim.geom.Conversions._
import java.awt.geom._
import java.awt._

object invalidationSpecs extends Specification {

  "Invalidated rect" should {
    "invalidate its region correctly." in {
      InvalidationHistory.clear
      InvalidationHistory.invalidate(new Rectangle(10, 10, 60, 60))
      InvalidationHistory.isInvalidated(new Rectangle(19, 19, 100, 20)) must be(true)
      InvalidationHistory.isInvalidated(new Rectangle(5, 5, 4, 300)) must be(false)
    }
  }

  "InvalidatedHistory" should {
    "give back a correct list of rects." in {
      InvalidationHistory.clear
      InvalidationHistory.invalidate(new Rectangle(10, 10, 60, 60))
      InvalidationHistory.invalidate(new Rectangle(60, 60, 10, 10))
      InvalidationHistory.intersectingRects(new Rectangle(55, 55, 10, 10)).length must_== 2
    }
  }

}
