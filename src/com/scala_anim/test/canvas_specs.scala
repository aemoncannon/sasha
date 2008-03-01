package com.scala_anim.test
import com.scala_anim._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.canvas.Canvas
import com.scala_anim.geom.Conversions._
import java.awt.geom._

object canvasSpecs extends Specification {

  "Canvas" should {
    "should be creatable" in {
      var c = new Canvas()
      c
    }
  }

  "Canvas" should {
    "should know if it contains points" in {
      var c = new Canvas()
      c.rect(0, 0, 20, 20)
      c.containsPoint((10, 10)) must be(true)
      c.containsPoint((19, 19)) must be(true)
      c.containsPoint((25, 19)) must be(false)
    }
  }

  "Circle on canvas" should {
    "contain points correctly" in {
      var c = new Canvas()
      c.circle(20, 20, 10)
      c.containsPoint((0, 0)) must be(false)
      c.containsPoint((9, 9)) must be(false)
      c.containsPoint((11, 11)) must be(false)
      c.containsPoint((15, 15)) must be(true)
      c.containsPoint((25, 25)) must be(true)
      c.containsPoint((29, 29)) must be(false)
    }
  }

  "Canvas bounds" should {
    "should update correctly." in {
      var c = new Canvas()
      c.rect(0, 0, 20, 20)
      c.rect(15, 15, 20, 20)
      c.width must_== 35
      c.height must_== 35
    }
  }

  "Canvas bounds" should {
    "should clear correctly." in {
      var c = new Canvas()
      c.rect(0, 0, 20, 20)
      c.rect(15, 15, 20, 20)
      c.clear()
      c.width must_== 0
      c.height must_== 0
    }
  }


  "Canvas " should {

    "calculate its globalBounds." in {
      val s = new Sprite()
      val c = s.canvas
      s.x = 100
      s.y = 100
      c.rect(20, 20, 20, 20)
      val g = s.canvas.globalBounds
      g.x must_== 120
      g.y must_== 120
      g.height must_== 20
      g.width must_== 20
    }

    "calculate its globalBounds with negative positions." in {
      val s = new Sprite()
      val c = s.canvas
      s.x = 100
      s.y = 100
      c.rect(-10, -10, 20, 20)
      c.rect(10, 0, 40, 40)
      val g = s.canvas.globalBounds
      g.x must_== 90
      g.y must_== 90
      g.width must_== 60
      g.height must_== 50
    }

    "calculate its globalBounds with scaling." in {
      val s = new Sprite()
      val c = s.canvas
      s.x = 100
      s.y = 100
      s.scaleX = 2
      s.scaleY = 2
      c.rect(-20, -20, 40, 40)
      val g = s.canvas.globalBounds
      g.x must_== 60
      g.y must_== 60
      g.width must_== 80
      g.height must_== 80
    }

  }

}
