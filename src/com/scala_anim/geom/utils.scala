package com.scala_anim.geom
import javax.swing._
import java.awt.event._
import java.awt._
import java.awt.geom._
import scala.collection.mutable.ArrayBuffer

object Utils{
  
  type Pt = Point2D.Float
  type Rect = Rectangle2D.Float
  type RoundRect = RoundRectangle2D.Float
  
  def union(rects:Seq[Rectangle2D]):Rectangle2D = {
    if(rects.isEmpty){
      new Rectangle2D.Float()
    }
    else{
      rects.foldLeft(rects(0).getBounds2D){ (soFar, r) =>
	Rectangle2D.union(soFar, r, soFar)
	soFar
      }
    }
  }


  def transformPoint(pt:Pt, t:AffineTransform):Point2D.Float = {
    var result:Point2D.Float = new Point2D.Float()
    t.transform(pt, result).asInstanceOf[Point2D.Float]
  }

  def inverseTransformPoint(pt:Pt, t:AffineTransform):Point2D.Float = {
    var result:Point2D.Float = new Point2D.Float()
    t.inverseTransform(pt, result).asInstanceOf[Point2D.Float]
  }

}
