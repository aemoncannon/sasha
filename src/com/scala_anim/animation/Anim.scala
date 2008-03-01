package com.scala_anim.animation

import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import scala.collection.mutable.ResizableArray
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.event._
import com.scala_anim.event.Event

object Anim {

  val UPDATE = 'UPDATE
  val FINISHED = 'FINISHED
  type Easer = ((Float, Float, Float, Float) => Float)

  def doWhile(tick:(=> Unit))(condition:(=> Boolean)) = {
    new FrameAnim(tick, condition)
  }

  def tween(from:Float, to:Float, duration:Int, setter:(Float => Unit), easer:Easer){
    var t:Int = 0
    doWhile{
      t += 1
      setter(easer(t, from, to - from, duration))
    }{ t < duration }
  }

  def tweenRotation(target:SceneTree, to:Float, duration:Int, easer:Easer) = {
    tween(target.rotation, to, duration, {r => target.rotation = r}, easer)
  }

  def tweenX(target:SceneTree, to:Float, duration:Int, easer:Easer) = {
    tween(target.x, to, duration, {x => target.x = x}, easer)
  }

  def tweenY(target:SceneTree, to:Float, duration:Int, easer:Easer) = {
    tween(target.y, to, duration, {y => target.y = y}, easer)
  }

  def tweenXY(target:SceneTree, toX:Float, toY:Float, duration:Int, easer:Easer) = {
    tween(target.x, toX, duration, {x => target.x = x}, easer)
    tween(target.y, toY, duration, {y => target.y = y}, easer)
  }

  class FrameAnim(tick:( => Unit), condition:( => Boolean)) extends EventDispatcher{
    val listener:(Event => Unit) = ((e:Event) =>
      if(condition){
	tick
	dispatchEvent(new Event(Anim.UPDATE))
      }
      else{
	Stage.stage.removeEventListener(PropagatingEvent.ENTER_FRAME, listener, true)
	dispatchEvent(new Event(Anim.FINISHED))
      }
    )
    Stage.stage.addEventListener(PropagatingEvent.ENTER_FRAME, listener, true)
  }

}

