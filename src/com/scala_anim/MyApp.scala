package com.scala_anim
import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import java.awt.geom.AffineTransform
import com.scala_anim.animation._

class MyApp(width:Int, height:Int) extends App(width, height) {

  override def initApp(){
    val b1 = new Bubble(60)
    b1.x = 200
    b1.y = 200
    stage += b1
  }

  override def stopApp(){
  }

}

