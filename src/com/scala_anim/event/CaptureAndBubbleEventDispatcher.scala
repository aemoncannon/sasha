package com.scala_anim.event
import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import scala.collection.mutable.HashMap
import scala.List

trait CaptureAndBubbleEventDispatcher extends EventDispatcher{
  
  private val bubbleListenerMap = new HashMap[Symbol, List[Event => Unit]]()
  private val captureListenerMap = new HashMap[Symbol, List[Event => Unit]]()

  override def addEventListenerInline(eventType:Symbol)(listener:(Event => Unit)){
    addEventListener(eventType, listener, false);
  }

  override def addEventListener(eventType:Symbol, listener:(Event => Unit)){
    addEventListener(eventType, listener, false)
  }

  override def removeEventListener(eventType:Symbol, listener:(Event => Unit)){
    removeEventListener(eventType, listener, false)
  }

  override def removeEventListenersOfType(eventType:Symbol){
    removeEventListenersOfType(eventType, false)
  }

  override def removeAllEventListeners(){
    bubbleListenerMap.clear()
    captureListenerMap.clear()
  }

  override def dispatchEvent(event:Event){
    dispatchEvent(event, false)
  }

  def removeEventListenersOfType(eventType:Symbol, capture:Boolean){
    val listenerMap = if(capture){captureListenerMap}else{bubbleListenerMap}
    listenerMap += eventType -> List()
  }

  def addEventListenerInline(eventType:Symbol, capture:Boolean)(listener:(Event => Unit)){
    addEventListener(eventType, listener, capture);
  }

  def addEventListener(eventType:Symbol, listener:(Event => Unit), capture:Boolean){
    val listenerMap = if(capture){captureListenerMap}else{bubbleListenerMap}
    val existingListeners = listenerMap.getOrElseUpdate(eventType, List())
    listenerMap += eventType -> (listener :: existingListeners)
  }

  def removeEventListener(eventType:Symbol, listener:(Event => Unit), capture:Boolean){
    val listenerMap = if(capture){captureListenerMap}else{bubbleListenerMap}
    val existingListeners = listenerMap.getOrElseUpdate(eventType, List())
    listenerMap += eventType -> (existingListeners.remove(f => f == listener))
  }

  def dispatchCaptureEvent(event:Event){
    dispatchEvent(event, true)
  }

  def dispatchEvent(event:Event, capture:Boolean){
    val listenerMap = if(capture){captureListenerMap}else{bubbleListenerMap}
    val listeners = listenerMap.getOrElseUpdate(event.eventType, List())
    for(ea <- listeners){
      ea(event)
    }
  }

  def listenersOfType(eventType:Symbol, capture:Boolean) = {
    val listenerMap = if(capture){captureListenerMap}else{bubbleListenerMap}
    listenerMap.getOrElseUpdate(eventType, List())
  }

}
