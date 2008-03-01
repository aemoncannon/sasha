package com.scala_anim.geom
import javax.swing._
import java.awt.event._
import java.awt._
import java.awt.geom._
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.geom.Utils._

object Conversions{

  implicit def tuple2Point2d(pt:(Float, Float)):Point2D.Float = {
    new Point2D.Float(pt._1, pt._2)
  }

  implicit def rectFloat2rect(r:Rectangle2D):Rect = {
    new Rect(r.getX.toFloat, r.getY.toFloat, r.getWidth.toFloat, r.getHeight.toFloat)
  }

}
