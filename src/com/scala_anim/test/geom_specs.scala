package com.scala_anim.test
import com.scala_anim._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.util._
import com.scala_anim.geom.Conversions._
import com.scala_anim.geom.Utils._
import java.awt.geom._
import java.awt.Rectangle

object geomSpecs extends Specification {

  "union" should {
    "correctly union a collection of rectangles" in {
      val r = union(List(
	  new Rectangle(10, 10, 20, 20), 
	  new Rectangle(5, 10, 50, 50)
	))
      r must beLike { case r:Rectangle2D if r.getX == 5 && r.getY == 10 && r.getWidth == 50 && r.getHeight == 50 => true }

      val r2 = union(List(
	  new Rectangle(10, 10, 20, 20), 
	  new Rectangle(25, 25, 10, 10)
	))
      r2 must beLike { case r:Rectangle2D if r.getX == 10 && r.getY == 10 && r.getWidth == 25 && r.getHeight == 25 => true }
    }
  }

}
