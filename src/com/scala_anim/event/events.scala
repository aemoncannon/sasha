package com.scala_anim.event
import java.awt.Point 
import com.scala_anim.geom.Utils._


class Event(val eventType:Symbol){

  var phase = 'CAPTURE

  var propagationStopped = false

  def stopPropagation(){
    propagationStopped = true
  }

  def relevantTo(tree:SceneTree):Boolean = {
    true
  }

}

object PropagatingEvent{
  val ENTER_FRAME = 'ENTER_FRAME
}
class PropagatingEvent(eventType:Symbol) extends Event(eventType){
}

object MouseEvent{
  val MOUSE_DOWN = 'MOUSE_DOWN
  val MOUSE_UP = 'MOUSE_UP
  val MOUSE_MOVED = 'MOUSE_MOVED
  val MOUSE_DRAGGED = 'MOUSE_DRAGGED
}
class MouseEvent(eventType:Symbol, val p:Pt) extends PropagatingEvent(eventType){
  override def relevantTo(tree:SceneTree):Boolean = { 
    tree.containsGlobalPoint(p)
  }
  def x = p.x
  def y = p.y
}


