package com.scala_anim
import java.awt.image.BufferedImage
import java.awt.Color
import scala.collection.mutable.ResizableArray
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.canvas.Canvas
import com.scala_anim.event._
import com.scala_anim.animation._

class Bubble(private val radius:Float) extends Sprite with Draggable {

  canvas.setFill(Color.blue, 0.25f)
  canvas.setStroke(Color.white, 2, 0.45f)
  canvas.circle(0, 0, radius)

  if(radius > 30){
    for(x <- (1 to 10)){
      val theta = ((2.0f * Math.Pi)/10) * x
      val child = new Bubble(radius/2)
      child.x = (Math.cos(theta) * 2 * radius).toFloat
      child.y = (Math.sin(theta) * 2 * radius).toFloat
      this += child
    }
  }

  val mouseUpListener:(Event => Unit) = {(e:Event) =>
    e.stopPropagation()
    stopDrag()
    val anim = Anim.tweenXY(this, preDragX, preDragY, 30, Easing.BOUNCE_OUT)
    Stage.stage.removeEventListener(MouseEvent.MOUSE_UP, mouseUpListener)
  }

  addEventListenerInline(MouseEvent.MOUSE_DOWN){ e =>
    e.stopPropagation
    startDrag(e)
    Stage.stage.addEventListener(MouseEvent.MOUSE_UP, mouseUpListener)
  }

}
