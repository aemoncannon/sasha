package com.scala_anim
import javax.swing._
import java.awt._

class ShowApplet extends JApplet {
  var app:MyApp = null;

  override def init() {
    app = new MyApp(getWidth(), getHeight());
    getContentPane().add(app);
  }

  override def start() {
    app.start();
  }
  
  override def stop() {
    app.stop();
    getContentPane().remove(app);
    app = null
  }

}

