package com.scala_anim
import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import java.awt.geom.AffineTransform
import com.scala_anim.animation._

class MyApp(width:Int, height:Int) extends App(width, height) {

  override def initApp(){
    //     for(i <- (1 to 20)){
    //       val c1 = new RectSprite(40, 40)
    //       c1.x = i * 50
    //       c1.y = i * 50
    //       stage += c1
    //     }

    val b1 = new Bubble(60)
    b1.x = 200
    b1.y = 200
    stage += b1

    val c1 = new RectSprite(30, 100)
    stage += c1
    c1.x = 300
    c1.y = 300
    Anim.tweenRotation(c1, Math.Pi.toFloat * 20.0f, 600, Easing.LINEAR_NONE)    
  }

  override def stopApp(){
  }

}

