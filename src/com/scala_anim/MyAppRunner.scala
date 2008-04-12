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

object MyAppRunner extends JFrame( "MyAppRunner" ) {    

  def main( args: Array[String] ) = {
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    val app = new MyApp(700, 500);
    add(app);
    pack();
    setBounds(400, 200, 700, 500)
    setVisible( true );
    app.start();
  }

}
