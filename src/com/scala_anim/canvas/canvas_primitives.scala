package com.scala_anim.canvas
import javax.swing._
import java.awt.event._
import java.awt._
import java.awt.geom._
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.geom.Utils._

object CanvasPrimitive{
  val defaultStroke = new BasicStroke(0)
}

trait CanvasPrimitive extends Shape {
  var alpha:Float = 1.0f
  def drawOn(g:Graphics2D)
  def bounds:Rect = {
    val b = getBounds2D
    val r = new Rect(b.getX.toFloat, b.getY.toFloat, b.getWidth.toFloat, b.getHeight.toFloat)
    r
  }
}

trait CanvasShapeFill extends CanvasPrimitive{
  var fillColor:Color = Color.gray
  def drawOn(g:Graphics2D){
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha))
    g.setColor(fillColor)
    g.fill(this)
  }
}

trait CanvasShapeStroke extends CanvasPrimitive{
  var strokeColor:Color = Color.black
  var stroke:BasicStroke = CanvasPrimitive.defaultStroke;
  def drawOn(g:Graphics2D){
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha))
    g.setStroke(stroke)
    g.setColor(strokeColor)
    g.draw(this)
  }
  override def bounds:Rect = {
    val b = super.bounds
    val w = stroke.getLineWidth
    new Rect(b.x - w/2.0f, b.y - w/2.0f, b.width + w*2.0f, b.height + w*2.0f)
  }
}

class RectFill(x:Float, y:Float, w:Float, h:Float) extends Rect(x, y, w, h) with CanvasShapeFill{}
class RectStroke(x:Float, y:Float, w:Float, h:Float) extends Rect(x, y, w, h) with CanvasShapeStroke{}

class RoundRectFill(x:Float, y:Float, w:Float, h:Float, cornerW:Float, cornerH:Float) extends RoundRect(x, y, w, h, cornerW, cornerH) with CanvasShapeFill{}
class RoundRectStroke(x:Float, y:Float, w:Float, h:Float, cornerW:Float, cornerH:Float) extends RoundRect(x, y, w, h, cornerW, cornerH) with CanvasShapeStroke{}

class OvalFill(x:Float, y:Float, w:Float, h:Float) extends Ellipse2D.Float(x, y, w, h) with CanvasShapeFill{}
class OvalStroke(x:Float, y:Float, w:Float, h:Float) extends Ellipse2D.Float(x, y, w, h) with CanvasShapeStroke{}

class PathFill(g:GeneralPath) extends ShapeWrapper(g) with CanvasShapeFill{}
class PathStroke(g:GeneralPath) extends ShapeWrapper(g) with CanvasShapeStroke{}

class ShapeWrapper(val shape:Shape) extends Shape{
  def contains(x:Double, y:Double):Boolean = { shape.contains(x,y) }
  def contains(x:Double, y:Double, w:Double, h:Double):Boolean = { shape.contains(x, y, w, h) }
  def contains(p:Point2D):Boolean = { shape.contains(p) }
  def contains(r:Rectangle2D):Boolean = { shape.contains(r) }
  def getBounds():Rectangle = { shape.getBounds() }
  def getBounds2D():Rectangle2D = { shape.getBounds2D() }
  def getPathIterator(t:AffineTransform):PathIterator = { shape.getPathIterator(t) }
  def getPathIterator(t:AffineTransform, flatness:Double):PathIterator = { shape.getPathIterator(t, flatness) }
  def intersects(x:Double, y:Double, w:Double, h:Double):Boolean = { shape.intersects(x, y, w, h) }
  def intersects(r:Rectangle2D):Boolean = { shape.intersects(r) }
}

