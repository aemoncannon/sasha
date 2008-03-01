package com.scala_anim.animation
import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import scala.collection.mutable.ResizableArray
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.event._
import com.scala_anim.event.Event

object Easing {

  def QUAD_IN(t:Float, b:Float, c:Float, d:Float) = {
    val t1 = t/d
    c*t1*t1 + b
  }

  def QUAD_OUT(t:Float, b:Float, c:Float, d:Float) = {
    val t1 = t/d
    -c *t1*(t1-2) + b
  }
  
  def QUAD_IN_OUT(t:Float, b:Float, c:Float, d:Float) = {
    val t1 = t/(d/2)
    if (t1 < 1) {
      c/2*t1*t1 + b
    } 
    else {
      -c/2 * ((t1 - 1) * (t1 - 3) - 1) + b
    }
  }

  def LINEAR_NONE(t:Float, b:Float, c:Float, d:Float):Float = {
    c*t/d + b;
  }

  def LINEAR_IN(t:Float, b:Float, c:Float, d:Float):Float = {
    c*t/d + b;
  }

  def LINEAR_OUT(t:Float, b:Float, c:Float, d:Float):Float = {
    c*t/d + b;
  }

  def LINEAR_IN_OUT (t:Float, b:Float, c:Float, d:Float):Float = {
    c*t/d + b;
  }


  def BOUNCE_OUT(t:Float, b:Float, c:Float, d:Float):Float = {
    val t1 = t/d
    if ((t1) < (1/2.75f)) {
      c * (7.5625f * t1 * t1) + b;
    } 
    else if (t1 < (2 / 2.75)) {
      val t2 = t1 - (1.5f / 2.75f)
      c * (7.5625f * t2 * t2 + 0.75f) + b;
    } 
    else if (t1 < (2.5 / 2.75)) {
      val t3 = t1 - (2.25f / 2.75f)
      c * (7.5625f * t3 * t3 + 0.9375f) + b;
    } 
    else {
      val t4 = t1 - (2.625f / 2.75f)
      c * (7.5625f * t4 * t4 + 0.984375f) + b;
    }
  }

  def BOUNCE_IN(t:Float, b:Float, c:Float, d:Float):Float = {
    c - BOUNCE_OUT(d-t, 0, c, d) + b;
  }

  def BOUNCE_IN_OUT(t:Float, b:Float, c:Float, d:Float):Float = {
    if (t < d/2) {
      BOUNCE_IN(t*2, 0.0f, c, d) * 0.5f + b;
    }
    else {
      BOUNCE_OUT(t*2-d, 0.0f, c, d) * 0.5f + c * 0.5f + b;
    }
  }

}

