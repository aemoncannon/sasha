package com.scala_anim.event
import javax.swing._
import java.awt.event._
import java.awt.image.BufferedImage
import java.awt._
import scala.collection.mutable.HashMap
import scala.List


trait EventDispatcher{

  type EventListener = Event => Unit

  private val listenerMap = new HashMap[Symbol, List[EventListener]]()

  def addEventListenerInline(eventType:Symbol)(listener:EventListener){
    addEventListener(eventType, listener);
  }

  def addEventListener(eventType:Symbol, listener:EventListener){
    val existingListeners = listenerMap.getOrElseUpdate(eventType, List())
    listenerMap += eventType -> (listener :: existingListeners)
  }

  def removeEventListener(eventType:Symbol, listener:EventListener){
    val existingListeners = listenerMap.getOrElseUpdate(eventType, List())
    listenerMap += eventType -> (existingListeners.remove(f => f == listener))
  }

  def removeEventListenersOfType(eventType:Symbol){
    listenerMap += eventType -> List()
  }


  def removeAllEventListeners(){
    listenerMap.clear()
  }

  def dispatchEvent(event:Event){
    val listeners = listenerMap.getOrElseUpdate(event.eventType, List())
    for(ea <- listeners){
      ea(event)
    }
  }

  def listenersOfType(eventType:Symbol) = {
    listenerMap.getOrElseUpdate(eventType, List())
  }

}
