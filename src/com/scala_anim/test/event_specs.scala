package com.scala_anim.test
import com.scala_anim._
import org.specs.runner.JUnit3
import org.specs.Specification
import com.scala_anim.event._

object eventSpecs extends Specification {

  "Events" should {
    "have event types" in {
      val e = new Event('DUDE)
      e.eventType must_== 'DUDE
    }
  }

  "EventDispatcher" should {
    "should register one listener" in {
      val disp = new EventDispatcher(){}
      var counter = 0
      disp.addEventListenerInline('ADD) { e =>
	counter += 1
      }
      disp.dispatchEvent(new Event('ADD))
      counter must_== 1
    }
  }

  "EventDispatcher" should {
    "should register one listener and remove it" in {
      val disp = new EventDispatcher(){}
      var counter = 0
      val listener = ((e:Event) => counter += 1)
      disp.addEventListener('ADD, listener)
      disp.listenersOfType('ADD).length must_== 1
      disp.removeEventListener('ADD, listener)
      disp.listenersOfType('ADD).length must_== 0
      disp.dispatchEvent(new Event('ADD))
      counter must_== 0
    }
  }

  "CaptureAndBubbleEventDispatcher" should {
    "should register one capture listener and remove it" in {
      val disp = new CaptureAndBubbleEventDispatcher(){}
      var counter = 0
      val listener = ((e:Event) => counter += 1)
      disp.addEventListener('ADD, listener, true)
      disp.listenersOfType('ADD, true).length must_== 1
      disp.removeEventListener('ADD, listener, true)
      disp.listenersOfType('ADD, true).length must_== 0
      disp.dispatchEvent(new Event('ADD), true)
      counter must_== 0
    }
  }


  "EventDispatcher" should {
    "should register one listener and remove all of that type" in {
      val disp = new EventDispatcher(){}
      var counter = 0
      disp.addEventListenerInline('ADD) { e =>
	counter += 1
      }
      disp.removeEventListenersOfType('ADD)
      disp.dispatchEvent(new Event('ADD))
      counter must_== 0
    }
  }

  "SceneTrees" should {
    "should propagate events" in {
      val tree = new Sprite()
      tree.addChild(new Sprite())
      var counter = 0
      tree.addEventListenerInline('ADD) { e =>
	counter += 1
      }
      tree.propagateEvent(new PropagatingEvent('ADD))
      counter must_== 1
    }

    "should be able to stopPropagation on bubble" in {
      var counter = 0
      val s1 = new Sprite()
      s1.addEventListenerInline('ADD) { e => counter += 1 }
      val s2 = new Sprite()
      s2.addEventListenerInline('ADD) { e => counter += 1; e.stopPropagation}
      val s3 = new Sprite()
      s3.addEventListenerInline('ADD) { e => counter += 1 }
      val s4 = new Sprite()
      s4.addEventListenerInline('ADD) { e => counter += 1 }
      s1 += (s2 += (s3 += s4))
      s1.propagateEvent(new PropagatingEvent('ADD))
      counter must_== 3
    }

    "should be able to stopPropagation on capture" in {
      var counter = 0
      val s1 = new Sprite()
      s1.addEventListenerInline('ADD, true) { e => counter += 1 }
      val s2 = new Sprite()
      s2.addEventListenerInline('ADD, true) { e => counter += 1; e.stopPropagation}
      val s3 = new Sprite()
      s3.addEventListenerInline('ADD, true) { e => counter += 1 }
      val s4 = new Sprite()
      s4.addEventListenerInline('ADD, true) { e => counter += 1 }
      s1 += (s2 += (s3 += s4))
      s1.propagateEvent(new PropagatingEvent('ADD))
      counter must_== 2
    }

    "should be able to stopPropagation on capture, when bubble and capture listeners exist." in {
      var counter = 0
      val s1 = new Sprite()
      s1.addEventListenerInline('ADD, true) { e => counter += 1 }
      s1.addEventListenerInline('ADD) { e => counter += 1 }
      val s2 = new Sprite()
      s2.addEventListenerInline('ADD, true) { e => counter += 1}
      s1.addEventListenerInline('ADD) { e => counter += 1 }
      val s3 = new Sprite()
      s3.addEventListenerInline('ADD, true) { e => counter += 1; e.stopPropagation }
      s1.addEventListenerInline('ADD) { e => counter += 1 }
      val s4 = new Sprite()
      s4.addEventListenerInline('ADD, true) { e => counter += 1 }
      s1.addEventListenerInline('ADD) { e => counter += 1 }
      s1 += (s2 += (s3 += s4))
      s1.propagateEvent(new PropagatingEvent('ADD))
      counter must_== 3
    }
  }

  "SceneTrees" should {
    "should propagate events and CAPTURE them correctly" in {
      val tree = new Sprite()
      tree.addChild(new Sprite())
      var counter = 0
      tree.addEventListenerInline('ADD, true) { e =>
	counter += 1
      }
      tree.propagateEvent(new PropagatingEvent('ADD))
      counter must_== 1
    }
  }

  "SceneTrees" should {
    "should propagate ENTER_FRAME events down from stage." in {
      val s = Stage.stage
      val c1 = new Sprite()
      c1.x = 10
      c1.y = 10
      val c2 = new Sprite()
      c2.x = 10
      c2.y = 10
      c1.addChild(c2)
      s.addChild(c1)
      var counter = 0
      c2.addEventListenerInline(PropagatingEvent.ENTER_FRAME, true) { e =>
	counter += 1
      }
      s.propagateEventCaptureOnly(new PropagatingEvent(PropagatingEvent.ENTER_FRAME))
      counter must_== 1
    }
  }

}
