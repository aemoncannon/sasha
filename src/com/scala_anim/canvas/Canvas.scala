package com.scala_anim.canvas
import javax.swing._
import java.awt.event._
import java.awt._
import java.awt.image._
import java.awt.geom._
import com.scala_anim.geom.Utils._
import com.scala_anim.geom.Conversions._
import scala.collection.mutable.ArrayBuffer

class Canvas(val owner:SceneTree) {
  private val shapes = new ArrayBuffer[CanvasPrimitive]()
  private var currentFillColor = Color.black
  private var currentStrokeColor = Color.black
  private var currentStroke = CanvasPrimitive.defaultStroke
  private var currentFillAlpha = 1.0f
  private var currentStrokeAlpha = 1.0f 
  private var canvasBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
  private val CANVAS_BUFFER_PADDING = 15
  private val identTransform = new AffineTransform()
  private val _bounds:Rectangle2D = new Rect();
  var needsRedraw = true
  var cacheAsBitmap = false

  def this(){
    this(Stage.backStage)
  }

  def bounds:Rect = _bounds.clone.asInstanceOf[Rect]

  private def recomputeBounds(){
    if(isBlank){
      _bounds.setRect(0, 0, 0, 0)
    }
    else{
      _bounds.setRect(shapes(0).bounds)
      for(shape <- shapes){
	Rectangle2D.union(_bounds, shape.bounds, _bounds)
      }
    }
  }

  def width:Float = {
    bounds.width
  }

  def height:Float = {
    bounds.height
  }

  def +=(c:CanvasPrimitive){
    shapes += c
    recomputeBounds()
    needsRedraw = true
    owner.canvasChanged()
  }

  def isBlank:Boolean = shapes.isEmpty

  def clear(){
    shapes.clear()
    recomputeBounds()
    needsRedraw = true
    owner.canvasChanged()
  }

  def globalBounds:Rect = {
    val b = bounds
    // Construct the minimal bounding box for the transformed bounds
    (owner.globalTransform.createTransformedShape(b)).getBounds2D
  }

  private def redrawBuffer(){
    val globalB = globalBounds
    canvasBuffer = new BufferedImage(globalB.width.toInt, globalB.height.toInt, BufferedImage.TYPE_INT_ARGB)
    val g = canvasBuffer.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.translate(-globalB.x, -globalB.y)
    g.transform(owner.globalTransform)
    for(ea <- shapes){
      ea.drawOn(g)
    }
    needsRedraw = false
  }

  private def drawCached(g:Graphics2D){
    if(needsRedraw){
      redrawBuffer()
    }
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER))
    g.setTransform(identTransform)
    val globalB = globalBounds
    g.translate(globalB.x, globalB.y)
    g.drawImage(canvasBuffer, identTransform, null)
  }

  def drawOn(g:Graphics2D){
    if(cacheAsBitmap){
      drawCached(g)
    }
    else{
      for(ea <- shapes){
	ea.drawOn(g)
      }
    }
  }

  def containsPoint(p:Pt):Boolean = {
    bounds.contains(p.x, p.y) && shapes.exists{ea => ea.contains(p.x, p.y)}
  }

  private def withAtts(p:CanvasShapeFill):CanvasPrimitive = {
    p.fillColor = currentFillColor
    p.alpha = currentFillAlpha
    p
  }

  private def withAtts(p:CanvasShapeStroke):CanvasPrimitive = {
    p.strokeColor = currentStrokeColor
    p.stroke = currentStroke
    p.alpha = currentStrokeAlpha
    p
  }

  def setAlpha(a:Float){
    currentStrokeAlpha = a
    currentFillAlpha = a
  }

  def setFill(c:Color){
    currentFillColor = c
  }
  def setFill(c:Color, a:Float){
    setFill(c)
    currentFillAlpha = a
  }

  def setStroke(c:Color){
    currentStrokeColor = c
  }
  def setStroke(c:Color, width:Float){
    setStroke(c);
    currentStroke = new BasicStroke(width);
  }
  def setStroke(c:Color, width:Float, alpha:Float){
    setStroke(c, width);
    currentStrokeAlpha = alpha
  }

  def rect(x:Float, y:Float, w:Float, h:Float){
    fillRect(x, y, w, h);
    strokeRect(x, y, w, h);
  }
  def strokeRect(x:Float, y:Float, w:Float, h:Float){
    this += withAtts(new RectStroke(x, y, w, h))
  }
  def fillRect(x:Float, y:Float, w:Float, h:Float){
    this += withAtts(new RectFill(x, y, w, h))
  }


  def roundRect(x:Float, y:Float, w:Float, h:Float, cornerW:Float, cornerH:Float){
    fillRoundRect(x, y, w, h, cornerW, cornerH);
    strokeRoundRect(x, y, w, h, cornerH, cornerW);
  }
  def strokeRoundRect(x:Float, y:Float, w:Float, h:Float, cornerW:Float, cornerH:Float){
    this += withAtts(new RoundRectStroke(x, y, w, h, cornerW, cornerH))
  }
  def fillRoundRect(x:Float, y:Float, w:Float, h:Float, cornerW:Float, cornerH:Float){
    this += withAtts(new RoundRectFill(x, y, w, h, cornerW, cornerH))
  }


  def oval(x:Float, y:Float, w:Float, h:Float){
    fillOval(x, y, w, h)
    strokeOval(x, y, w, h)
  }
  def strokeOval(x:Float, y:Float, w:Float, h:Float){
    this += withAtts(new OvalStroke(x, y, w, h))
  }
  def fillOval(x:Float, y:Float, w:Float, h:Float){
    this += withAtts(new OvalFill(x, y, w, h))
  }

  def circle(x:Float, y:Float, r:Float){
    fillCircle(x, y, r) 
    strokeCircle(x, y, r)
  }
  def strokeCircle(x:Float, y:Float, r:Float){
    this += withAtts(new OvalStroke(x - r, y - r, r * 2, r * 2))
  }
  def fillCircle(x:Float, y:Float, r:Float){
    this += withAtts(new OvalFill(x - r, y - r, r * 2, r * 2))
  }

  def path(func:(PathHelper => Unit)){
    val p = new PathHelper()
    func(p)
    this += withAtts(new PathFill(p.generalPath))
    this += withAtts(new PathStroke(p.generalPath))
  }

  def strokePath(func:(PathHelper => Unit)){
    val p = new PathHelper()
    func(p)
    this += withAtts(new PathStroke(p.generalPath))
  }

  def fillPath(func:(PathHelper => Unit)){
    val p = new PathHelper()
    func(p)
    this += withAtts(new PathFill(p.generalPath))
  }

}
