package com.scala_anim.canvas
import javax.swing._
import java.awt.geom._

class PathHelper() {

  val generalPath:GeneralPath = new GeneralPath()
  generalPath.moveTo(0, 0)
  
  def moveTo(x:Float, y:Float){
    generalPath.moveTo(x, y)
  }

  def lineTo(x:Float, y:Float){
    generalPath.lineTo(x, y)
  }

  def curveTo(x1:Float, y1:Float, x2:Float, y2:Float, x3:Float, y3:Float){
    generalPath.curveTo(x1, y1, x2, y2, x3, y3)
  }

  def close(){
    generalPath.closePath()
  }

}
