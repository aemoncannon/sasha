package com.scala_anim
import java.awt.image.BufferedImage
import java.awt.Color
import scala.collection.mutable.ResizableArray
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.canvas.Canvas
import com.scala_anim.event._

class RectSprite(w:Float, h:Float) extends Sprite {
  canvas.setFill(Color.red, 1)
  canvas.setStroke(Color.white, 1, 1.0f)
  canvas.rect(0, 0, w, h);
}
class RectSpriteNoStroke(w:Float, h:Float) extends Sprite {
  canvas.setFill(Color.red, 1)
  canvas.fillRect(0, 0, w, h);
}
class SquareSprite(s:Float) extends RectSprite(s, s)

class OvalSprite(w:Float, h:Float) extends Sprite{
  canvas.setFill(Color.red, 1)
  canvas.setStroke(Color.white, 1, 1.0f)
  canvas.oval(0, 0, w, h);
}
class CircleSprite(r:Float) extends OvalSprite(r, r)
