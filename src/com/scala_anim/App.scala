package com.scala_anim
import javax.swing._
import java.awt.RenderingHints
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt._
import java.awt.geom.AffineTransform
import scala.collection.mutable.ArrayBuffer
import com.scala_anim.event._
import com.scala_anim.util._
import com.scala_anim.geom.Conversions._

object App{
  val CLIP_PADDING = 2
  private val invalidatedRectsQ = new ArrayBuffer[Rectangle]()
  def withInvalidatedRectsQ(func:(ArrayBuffer[Rectangle] => Unit)) = synchronized{ func(invalidatedRectsQ) }

  private val propagatingEventsQ = new ArrayBuffer[PropagatingEvent]()
  def withPropagatingEventsQ(func:(ArrayBuffer[PropagatingEvent] => Unit)) = synchronized{ func(propagatingEventsQ) }
}

abstract class App(width:Int, height:Int) extends JPanel with Runnable with MouseListener with MouseMotionListener{
  var frontGraphics:Graphics2D = null;
  var backImage:BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  var backGraphics:Graphics2D = (backImage.getGraphics()).asInstanceOf[Graphics2D]
  backGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  backGraphics.setBackground(Color.black)
  val identTransform:AffineTransform = new AffineTransform()

  /* Create primary update thread.. */
  val thread = new Thread(this);
  thread.setPriority(Thread.MAX_PRIORITY);
  var continue = true;

  /* Initialize the global, singleton stage (root of the scene tree). */
  val stage = Stage.resetStage()
  stage.resize(width, height)

  protected def initGraphics(){
    frontGraphics = getGraphics().asInstanceOf[Graphics2D]
    addMouseListener(this)
    addMouseMotionListener(this)
  }

  protected def initApp()

  protected def stopApp()

  def start() {
    initGraphics()
    initApp()
    thread.start();
  }
  
  def stop() = synchronized {
    stopApp()
    removeMouseListener(this)
    removeMouseMotionListener(this)
    continue = false;
  }
  
  def run() {
    continue = true;
    while (continue) {
      App.withPropagatingEventsQ{ q =>
	for(e <- q){
	  stage.propagateEvent(e)
	}
	q.clear()
      }
      stage.propagateEventCaptureOnly(new PropagatingEvent(PropagatingEvent.ENTER_FRAME))

      App.withInvalidatedRectsQ{ q =>
	for(r <- q){
	  InvalidationHistory.invalidate(r)
	}
	q.clear()
      }

      redraw()

      try {
	Thread.sleep(10)
      }
      catch {
	case e: InterruptedException => 
	{ continue = false; }
      }
    }
  }

  def redraw() = synchronized {
    backGraphics.setTransform(identTransform)
    stage.redraw(backGraphics)
    val inval = InvalidationHistory.invalidatedRectsUnion
    frontGraphics.setClip(
      inval.getX.toInt - App.CLIP_PADDING, 
      inval.getY.toInt - App.CLIP_PADDING, 
      inval.getWidth.toInt + 2 * App.CLIP_PADDING, 
      inval.getHeight.toInt + 2 * App.CLIP_PADDING)
    frontGraphics.drawImage(backImage, 0, 0, null)
    InvalidationHistory.clear()
  }

  override def paintAll(g:Graphics){
    App.withInvalidatedRectsQ{ q =>
      q += new Rectangle(0, 0, width, height)
    }
  }

  override def paint(g:Graphics){
    App.withInvalidatedRectsQ{ q =>
      q += new Rectangle(0, 0, width, height)
    }
  }

  override def repaint(){
    App.withInvalidatedRectsQ{ q =>
      q += new Rectangle(0, 0, width, height)
    }
  }

  override def repaint(tm:Long){
    App.withInvalidatedRectsQ{ q =>
      q += new Rectangle(0, 0, width, height)
    }
  }

  override def repaint(r:Rectangle){
    App.withInvalidatedRectsQ{ q =>
      q += r
    }
  }

  override def repaint(x:Int, y:Int, width:Int, height:Int){
    val r = new Rectangle(x, y, width, height);
    App.withInvalidatedRectsQ{ q =>
      q += r
    }
  }


  private implicit def toMouseEvent(e:java.awt.event.MouseEvent):MouseEvent = {
    e.getID() match{
      case java.awt.event.MouseEvent.MOUSE_PRESSED => new MouseEvent(MouseEvent.MOUSE_DOWN, (e.getX(), e.getY()))
      case java.awt.event.MouseEvent.MOUSE_RELEASED => new MouseEvent(MouseEvent.MOUSE_UP, (e.getX(), e.getY()))
      case java.awt.event.MouseEvent.MOUSE_DRAGGED => new MouseEvent(MouseEvent.MOUSE_DRAGGED, (e.getX(), e.getY()))
      case java.awt.event.MouseEvent.MOUSE_MOVED => new MouseEvent(MouseEvent.MOUSE_MOVED, (e.getX(), e.getY()))
      case _ => new MouseEvent('UNKNOWN_MOUSE_EVENT_TYPE, (e.getX(), e.getY()))
    }
  }

  /* implement MouseMotionListener */

  def mouseDragged(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }  

  def mouseMoved(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }  

  /* implement MouseListener */

  def mousePressed(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }

  def mouseReleased(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }

  def mouseEntered(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }

  def mouseExited(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }

  def mouseClicked(e:java.awt.event.MouseEvent) {
    App.withPropagatingEventsQ{ q =>
      q += e
    }
  }

}
