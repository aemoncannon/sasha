package com.scala_anim.test
import com.scala_anim._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.event._
import com.scala_anim.geom.Conversions._
import com.scala_anim.geom.Utils._

object sceneTreeSpecs extends Specification {

  "Scene tree with stage" should {
    "have chain to stage" in {
      val s = Stage.stage
      val c = new Sprite()
      s.addChild(c)
      val chain = c.parentChainToStage
      chain must beLike { case List(s, c) => true }
    }
  }

  "Scene tree with stage" should {
    "have complete chain to stage" in {
      val s = Stage.stage
      val c1 = new Sprite()
      val c2 = new Sprite()
      c1.addChild(c2)
      s.addChild(c1)
      val chain = c2.parentChainToStage
      chain must beLike { case List(s, c1, c2) => true }
    }
  }

  "A nested tree" should {
    "be able to convert a point to local coordinates" in {
      val s = Stage.stage
      val c1 = new Sprite()
      c1.x = 10
      c1.y = 10
      val c2 = new Sprite()
      c2.x = 10
      c2.y = 10
      s += c1 
      c1 += c2
      val p = c2.globalToLocal((25, 25))
      p must beLike { case p:Pt if p.x == 5.0 && p.y == 5.0 => true }
    }
  }

  "A nested tree" should {
    "be able to convert a local point to global coordinates" in {
      val s = Stage.stage
      val c1 = new Sprite()
      c1.x = 10
      c1.y = 10
      val c2 = new Sprite()
      c2.x = 10
      c2.y = 10
      c1.addChild(c2)
      s.addChild(c1)
      val p = c2.localToGlobal((5, 5))
      p must beLike { case p:Pt if p.x == 25.0 && p.y == 25.0 => true }
    }
  }

  "A newly created tree" should {
    "convert a point to local coordinates" in {
      val c = new Sprite()
      val p = c.globalToLocal((25, 25))
      p must beLike { case p:Pt if p.x == 25.0 && p.y == 25.0 => true }
    }
  }


  "When added to a stage-rooted tree, all subtree's of child " should {
    "now be stage-rooted." in {
      val s = Stage.stage
      val s1 = new Sprite()
      val s2 = new Sprite()
      val s3 = new Sprite()
      s1.addChild(s2)
      s2.addChild(s3)
      s.addChild(s1)
      s.forallDesc{ ea => ea.stage == s } must be(true)
    }

    "now be stage-rooted(even when added in abbrev form)." in {
      val s = Stage.stage
      val s1 = new Sprite()
      val s2 = new Sprite()
      val s3 = new Sprite()
      s += (s1 += (s2 += (s3)))
      s.forallDesc{ ea => ea.stage == s } must be(true)
    }
  }


  "A newly created tree's parent" should {
    "be the backStage." in {
      val c = new Sprite()
      c.parent must_== Stage.backStage
    }
  }


  "The stage's stage " should {
    "be the stage." in {
      Stage.stage.stage must_== Stage.stage
    }
  }

  "The stage's parent " should {
    "be the stage." in {
      Stage.stage.parent must_== Stage.stage
    }
  }


  "The backStage's stage " should {
    "be the stage." in {
      Stage.backStage.stage must_== Stage.stage
    }
  }


  "The backStage's parent " should {
    "be the stage." in {
      Stage.backStage.parent must_== Stage.stage
    }
  }


  "A newly created tree's stage" should {
    "be the stage." in {
      val c = new Sprite()
      c.stage must_== Stage.stage
    }
  }

  "removeChild" should {
    "return the parent." in {
      val c = new Sprite()
      val c1 = new Sprite()
      c.addChild(c1)
      val c2 = c.removeChild(c1)
      c2 must_== c
    }
  }

  "A removed tree" should {
    "be added to the backstage." in {
      val c = new Sprite()
      c.parent must_== Stage.backStage
      c.stage must_== Stage.stage
      val s = Stage.stage
      s.addChild(c)
      c.stage must_== Stage.stage
      val c1 = s.removeChild(c)
      c must beLike { case thing:SceneTree if thing.parent == Stage.backStage => true }
    }
  }


  "A tree's global bounds" should {
    "take stroke into account." in {
      val c = new RectSprite(10, 10)
      Stage.stage += c
      c.globalBounds.x must_== -0.5
      c.globalBounds.y must_== -0.5
      c.globalBounds.width must_== 11.0
      c.globalBounds.height must_== 11.0
    }
  }


  "A tree with an empty canvas" should {
    "only consider children when computing its bounds" in {
      val s = Stage.stage
      val p = new Sprite()
      p.x = 0
      p.y = 0
      val c = new RectSpriteNoStroke(10, 10)
      c.x = 10
      c.y = 10
      p.addChild(c)
      s.addChild(p)

      c.globalBounds.width must_== 10
      c.globalBounds.height must_== 10
      c.globalBounds.x must_== 10
      c.globalBounds.y must_== 10

      p.canvas.rect(0, 0, 1, 1)

      p.globalBounds.width must_== 20
      p.globalBounds.height must_== 20
      p.globalBounds.x must_== 0
      p.globalBounds.y must_== 0

      p.canvas.clear()

      p.globalBounds.width must_== 10
      p.globalBounds.height must_== 10
      p.globalBounds.x must_== 10
      p.globalBounds.y must_== 10

      p.removeChild(c)

      p.globalBounds.width must_== 0
      p.globalBounds.height must_== 0
      p.globalBounds.x must_== 0
      p.globalBounds.y must_== 0
      
    }
  }

  "A transplanted tree" should {
    "have its bounds updated." in {
      val child = new RectSpriteNoStroke(10, 10)
      child.x = 5
      child.y = 5
      val parent = new RectSpriteNoStroke(20, 20)
      parent.scaleX = 2
      parent.x = 10
      parent.y = 10
      val s = Stage.stage
      s.addChild(parent)
      parent.addChild(child)

      child.globalBounds.width must_== 20
      child.globalBounds.height must_== 10
      child.globalBounds.x must_== 20
      child.globalBounds.y must_== 15
      
      child.transplantTo(s)

      child.globalBounds.width must_== 10
      child.globalBounds.height must_== 10
      child.globalBounds.x must_== 5
      child.globalBounds.y must_== 5

      parent.transplantTo(child)

      parent.globalBounds.width must_== 40
      parent.globalBounds.height must_== 20
      parent.globalBounds.x must_== 15
      parent.globalBounds.y must_== 15
    }
  }

  "A scaled tree" should {
    "have its bounds and the bounds of its children updated." in {
      val child = new RectSpriteNoStroke(10, 10)
      child.x = 5
      child.y = 5
      val parent = new RectSpriteNoStroke(20, 20)
      parent.x = 10
      parent.y = 10
      val s = Stage.stage
      s.addChild(parent)
      parent.addChild(child)

      child.globalBounds.width must_== 10
      child.globalBounds.height must_== 10
      child.globalBounds.x must_== 15
      child.globalBounds.y must_== 15

      parent.globalBounds.width must_== 20
      parent.globalBounds.height must_== 20
      parent.globalBounds.x must_== 10
      parent.globalBounds.y must_== 10

      parent.scaleX = 2

      child.globalBounds.width must_== 20
      child.globalBounds.height must_== 10
      child.globalBounds.x must_== 20
      child.globalBounds.y must_== 15

      parent.globalBounds.width must_== 40
      parent.globalBounds.height must_== 20
      parent.globalBounds.x must_== 10
      parent.globalBounds.y must_== 10
    }
  }


  "Transplanting a tree" should {
    "modify the parent bounds." in {
      val child = new RectSpriteNoStroke(10, 10)
      child.x = 5
      child.y = 5
      val parent = new RectSpriteNoStroke(10, 10)
      parent.x = 0
      parent.y = 0
      val s = Stage.stage
      s.addChild(parent)
      parent.addChild(child)

      parent.globalBounds.width must_== 15
      parent.globalBounds.height must_== 15
      parent.globalBounds.x must_== 0
      parent.globalBounds.y must_== 0

      child.transplantTo(s)

      parent.globalBounds.width must_== 10
      parent.globalBounds.height must_== 10
      parent.globalBounds.x must_== 0
      parent.globalBounds.y must_== 0
    }
  }

  "Constructing a tree" should {
    "update the stage and parent for all children." in {
      val s = Stage.stage
      val s1 = new RectSpriteNoStroke(10, 10)
      val s2 = new RectSpriteNoStroke(10, 10)
      val s3 = new RectSpriteNoStroke(10, 10)
      val s4 = new RectSpriteNoStroke(10, 10)

      s += (s1 += (s2 += (s3 += s4)))

      s1.stage must_== s
      s1.parent must_== s

      s2.stage must_== s
      s2.parent must_== s1

      s3.stage must_== s
      s3.parent must_== s2

      s4.stage must_== s
      s4.parent must_== s3
    }
  }


  "A nested child" should {
    "know if it contains a global point." in {
      val s = Stage.stage
      val s1 = new RectSpriteNoStroke(10, 10)
      val s2 = new RectSpriteNoStroke(10, 10)
      val s3 = new RectSpriteNoStroke(10, 10)
      val s4 = new RectSpriteNoStroke(10, 10)
      s += s1
      s1 += s2
      s2 += s3
      s3 += s4
      s4.containsGlobalPoint((5, 5)) must be(true)
      s1.x = 10
      s2.x = 10
      s3.x = 10
      s4.x = 10
      s4.containsGlobalPoint((45, 5)) must be(true)
    }

    "know if it contains a global point, in abbrev form." in {
      val s = Stage.stage
      val s1 = new RectSpriteNoStroke(10, 10)
      val s2 = new RectSpriteNoStroke(10, 10)
      val s3 = new RectSpriteNoStroke(10, 10)
      val s4 = new RectSpriteNoStroke(10, 10)
      s += (s1 += (s2 += (s3 += s4)))
      s4.containsGlobalPoint((5, 5)) must be(true)
      s1.x = 10
      s2.x = 10
      s3.x = 10
      s4.x = 10
      s4.containsGlobalPoint((45, 5)) must be(true)
    }

    "know if it contains a global point, even with negative bounds." in {
      val s = Stage.stage
      val s1 = new RectSpriteNoStroke(20, 20)
      s1.x = 200
      s1.y = 200
      val s2 = new Sprite()
      s2.canvas.rect(-20, -20, 10, 10)
      val s3 = new Sprite()
      s3.canvas.rect(-20, -20, 10, 10)
      s += (s1 += (s2 += s3))
      s3.containsGlobalPoint((185, 185)) must be(true)
    }
  }

}
