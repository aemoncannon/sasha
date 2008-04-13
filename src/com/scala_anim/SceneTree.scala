package com.scala_anim
import java.awt.Color
import java.awt._
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.awt.geom._
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.event._
import com.scala_anim.canvas.Canvas
import com.scala_anim.geom.Conversions._
import com.scala_anim.geom.Utils._
import com.scala_anim.util._
import scala.List
import scala.collection.mutable.HashMap

trait SceneTree extends Collection[SceneTree] with CaptureAndBubbleEventDispatcher{
  private val transformWRTGlobal:AffineTransform = new AffineTransform()
  private val transformWRTLocal:AffineTransform = new AffineTransform()
  private val bounds:Rect = new Rect()
  private val boundsWRTParent:Rect = new Rect()
  private val boundsWRTGlobal:Rect = new Rect()
  private val workingTransform:AffineTransform = new AffineTransform()
  private val identTransform:AffineTransform = new AffineTransform()
  private var invalidated:Boolean = true;
  val canvas:Canvas = new Canvas(this);
  private var _x:Float = 0
  private var _y:Float = 0
  private var _rotation:Float = 0
  private var _scaleX:Float = 1
  private var _scaleY:Float = 1
  private val children:ArrayBuffer[SceneTree] = new ArrayBuffer[SceneTree]()

  private var _parent:SceneTree = null
  addToInitialParent()

  protected def addToInitialParent(){
    Stage.backStage += this
  }

  def parent = _parent

  def stage = Stage.stage

  def isStage = false

  def elements = children.elements

  def size = children.length

  def x_=(n:Float){ 
    preTransformUpdate()
    _x = n 
    postTransformUpdate()
  }
  def x = _x

  def y_=(n:Float){ 
    preTransformUpdate()
    _y = n
    postTransformUpdate()
  }
  def y = _y

  def scaleX_=(n:Float){
    preTransformUpdate() 
    _scaleX = n 
    postTransformUpdate()
  }
  def scaleX = _scaleX

  def scaleY_=(n:Float){ 
    preTransformUpdate()
    _scaleY = n 
    postTransformUpdate()
  }
  def scaleY = _scaleY

  def rotation_=(n:Float){ 
    preTransformUpdate()
    _rotation = n 
    postTransformUpdate()
  }
  def rotation = _rotation

  def removeAllChildren():SceneTree = {
    for(child <- this){
      Stage.backStage.addChild(child)
    }
    children.clear()
    InvalidationHistory.invalidate(boundsWRTGlobal)
    invalidated = true;
    this
  }

  def -=(child:SceneTree):SceneTree = {
    removeChild(child)
  }
  def removeChild(child:SceneTree):SceneTree = {
    val i = children.indexOf(child)
    if(i != -1) {
      Stage.backStage.addChild(child)
      InvalidationHistory.invalidate(boundsWRTGlobal)
      invalidated = true;
    }
    this
  }

  def +=(child:SceneTree):SceneTree = {
    addChild(child)
  }
  def addChild(child:SceneTree):SceneTree = {
    if(child._parent != null){
      val oldParent = child._parent
      val i = oldParent.children.indexOf(child)
      oldParent.children.remove(i)
      oldParent.recomputeBoundsGoingUp()
    }
    children += child
    child._parent = this
    InvalidationHistory.invalidate(boundsWRTGlobal)
    child.recomputeChildrenAfterParentChange()
    recomputeBoundsGoingUp()
    InvalidationHistory.invalidate(boundsWRTGlobal)
    invalidated = true;
    this
  }

  def parentChainToStage:List[SceneTree] = {
    var parentChain = List(this)
    foreachAncestor{ p =>
      parentChain = p :: parentChain
    }
    parentChain
  }

  def globalBounds:Rect = new Rect(boundsWRTGlobal.x, boundsWRTGlobal.y, boundsWRTGlobal.width, boundsWRTGlobal.height)
  def globalTransform = new AffineTransform(transformWRTGlobal)

  def transplantTo(newParent:SceneTree){
    parent.removeChild(this)
    newParent.addChild(this)
  }

  def foreachAncestor(func:(SceneTree => Unit)){
    var p:SceneTree = this
    if(!p.isStage){
      while(!p.parent.isStage){
	p = p.parent
	func(p)
      }
      func(p.stage)
    }
  }

  def foreachDesc(func:(SceneTree => Unit)){
    for(child <- this){
      func(child)
      child.foreachDesc(func)
    }
  }

  def foreachDescDepthFirst(func:(SceneTree => Unit)){
    for(child <- this){
      child.foreachDescDepthFirst(func)
      func(child)
    }
  }

  def forallDesc(func:(SceneTree => Boolean)):Boolean = {
    forall{ child => child.forallDesc(func) }
  }

  def existsDesc(func:(SceneTree => Boolean)):Boolean = {
    exists{ child => child.existsDesc(func) }
  }


  def redraw(g:Graphics2D){
    if(invalidated){
      redrawCompletely(g)
    }
    else{
      val intersecting = InvalidationHistory.intersectingRects(boundsForClipping)
      if(!intersecting.isEmpty){
	redrawPartially(g, union(intersecting))
      }
    }
  }

  protected def boundsForClipping = {
    boundsWRTGlobal
  }

  protected def drawGlobalBounds(g:Graphics2D){
    g.setClip(null)
    g.setTransform(identTransform)
    g.setStroke(new BasicStroke(1))
    g.setColor(Color.green)
    g.draw(new Rect(boundsWRTGlobal.x, boundsWRTGlobal.y, boundsWRTGlobal.width, boundsWRTGlobal.height))
  }

  protected def redrawPartially(g:Graphics2D, clip:Rect) {
    redrawCanvasPartially(g, clip)
    for(child <- this){
      if(clip.intersects(child.boundsForClipping)){
	child.redrawPartially(g, clip)
      }
    }
    invalidated = false;
  }

  private def redrawCanvasPartially(g:Graphics2D, clip:Rect){
    //    drawGlobalBounds(g)
    g.setClip(null)
    g.setTransform(identTransform)
    g.setClip(
      clip.getX.toInt - App.CLIP_PADDING,
      clip.getY.toInt - App.CLIP_PADDING, 
      clip.getWidth.toInt + App.CLIP_PADDING * 2, 
      clip.getHeight.toInt + App.CLIP_PADDING * 2
    )
    g.setTransform(transformWRTGlobal)
    canvas.drawOn(g)
  }

  protected def redrawCompletely(g:Graphics2D) {
    redrawCanvasCompletely(g)
    for(child <- this){
      child.redrawCompletely(g)
    }
    invalidated = false;
  }

  private def redrawCanvasCompletely(g:Graphics2D){
    g.setClip(null)
    g.setTransform(transformWRTGlobal)
    canvas.drawOn(g)
  }

  def globalToLocal(pt:Pt):Pt = {
    inverseTransformPoint(pt, transformWRTGlobal)
  }

  def localToGlobal(pt:Pt):Pt = {
    transformPoint(pt, transformWRTGlobal)
  }

  def originGlobalized:Pt = {
    localToGlobal((0,0))
  }

  def containsGlobalPoint(pt:Pt):Boolean = {
    containsPoint(globalToLocal(pt))
  }

  def containsPoint(pt:Pt):Boolean = {
    (bounds.contains(pt) && 
      (canvas.containsPoint(pt) || 
	exists{ child => 
	  val childPt = inverseTransformPoint(pt, child.transformWRTLocal)
	  child.containsPoint(childPt)
	}))
  }

  def propagateEvent(e:PropagatingEvent){
    e.phase = 'CAPTURE
    dispatchCaptureEvent(e)
    if(e.propagationStopped){return}
    for(child <- this){
      if(e.relevantTo(child)){
	child.propagateEvent(e)
      }
    }
    e.phase = 'BUBBLE
    if(e.propagationStopped){return}
    dispatchEvent(e)
  }

  def propagateEventCaptureOnly(e:PropagatingEvent){
    e.phase = 'CAPTURE
    dispatchCaptureEvent(e)
    if(e.propagationStopped){return}
    for(child <- this){
      if(e.relevantTo(child)){
	child.propagateEventCaptureOnly(e)
      }
    }
  }

  def dispose(){
    foreachDescDepthFirst{ child =>
      child.dispose()
    }
    removeAllEventListeners()
    removeAllChildren()
  }

  def canvasChanged(){
    recomputeBoundsGoingUp()
  }

  /***** Computing cached values ******/

  private def preTransformUpdate(){
    InvalidationHistory.invalidate(boundsWRTGlobal)
  }

  private def postTransformUpdate(){
    recomputeChildrenAfterParentChange()
    recomputeBoundsGoingUp()
    InvalidationHistory.invalidate(boundsWRTGlobal)
    invalidated = true;
  }

  private def recomputeBoundsGoingUp(){
    recomputeBounds()
    foreachAncestor{ a =>
      a.recomputeBounds()
    }
  }


  private def recomputeChildrenAfterParentChange(){
    recomputeTransformsWRTLocal()
    recomputeTransformsWRTGlobal()
    for(child <- this){ 
      child.recomputeChildrenAfterParentChange() 
    }
    recomputeBounds()
  }

  private def recomputeTransformsWRTGlobal(){
    transformWRTGlobal.setTransform(parent.transformWRTGlobal)
    transformWRTGlobal.translate(x, y)
    transformWRTGlobal.rotate(rotation)
    transformWRTGlobal.scale(scaleX, scaleY)
  }

  private def recomputeTransformsWRTLocal(){
    transformWRTLocal.setTransform(identTransform)
    transformWRTLocal.translate(x, y)
    transformWRTLocal.rotate(rotation)
    transformWRTLocal.scale(scaleX, scaleY)
  }

  private def recomputeBounds(){
    if(!canvas.isBlank){
      bounds.setRect(canvas.bounds)
    }
    else if(canvas.isBlank && this.isEmpty){
      bounds.setRect(0, 0, 0, 0)
    }
    else if(canvas.isBlank && !this.isEmpty){
      val childB = children(0).boundsWRTParent
      bounds.setRect(childB)
    }

    for(child <- this){
      val childB = child.boundsWRTParent
      Rectangle2D.union(bounds, childB, bounds)
    }

    recomputeBoundsWRTParent()
    recomputeBoundsWRTGlobal()
  }

  private def recomputeBoundsWRTParent(){
    // TODO, optimize and generalize this chunk
    val t = new AffineTransform()
    t.translate(x, y)
    t.rotate(rotation)
    t.scale(scaleX, scaleY)
    val transB = t.createTransformedShape(bounds)
    val newBounds:Rect = transB.getBounds2D()
    boundsWRTParent.setRect(newBounds);
  }

  private def recomputeBoundsWRTGlobal(){
    // TODO, optimize and generalize this chunk
    val transBG = transformWRTGlobal.createTransformedShape(bounds)
    val newBoundsG:Rect = transBG.getBounds2D()
    boundsWRTGlobal.setRect(newBoundsG)
  }

}
