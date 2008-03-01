package com.scala_anim.util
import javax.swing._
import java.awt.event._
import java.awt._
import java.awt.geom._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.List
import com.scala_anim.geom.Utils._

object InvalidationHistory{
  
  private val invalidatedSceneTrees = new HashMap[SceneTree, Boolean]()
  private val invalidatedRects =  new ArrayBuffer[Rectangle2D]()

  def invalidate(rect:Rectangle2D){
    invalidatedRects += new Rectangle(rect.getX.toInt, rect.getY.toInt, rect.getWidth.toInt, rect.getHeight.toInt);
  }

  def intersectingRects(rect:Rectangle2D):Seq[Rectangle2D] = {
    invalidatedRects.filter{ r => r.intersects(rect) }
  }

  def invalidatedRectsUnion():Rectangle2D = {
    union(invalidatedRects)
  }

  def isInvalidated(rect:Rectangle2D):Boolean = {
    invalidatedRects.exists{ r => r.intersects(rect) }
  }

  def clear(){
    invalidatedRects.clear
    invalidatedSceneTrees.clear
  }

}
