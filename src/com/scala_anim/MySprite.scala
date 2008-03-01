package com.scala_anim
import java.awt.image.BufferedImage
import java.awt.Color
import scala.collection.mutable.ResizableArray
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.canvas.Canvas
import com.scala_anim.event._

class MySprite() extends Sprite with Draggable {
  paint()

  addEventListener(PropagatingEvent.ENTER_FRAME, onEnterFrame, true)

  addEventListenerInline(MouseEvent.MOUSE_DOWN){ e =>
    e.stopPropagation
    startDrag(e)
  }

  Stage.stage.addEventListenerInline(MouseEvent.MOUSE_UP){ e =>
    e.stopPropagation
    stopDrag()
  }

  def paint(){
    canvas.setFill(Color.red, 0.75f)
    canvas.setStroke(Color.white, 1, 1.0f)
    canvas.circle(0, 0, 15)
  }

  def onEnterFrame(e:Event){
  }

}
