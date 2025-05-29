package de.htwg.se.rummikub.aview.gui

import scala.swing._
import scala.swing.event._

import java.awt.Font
import java.awt.Dimension

import de.htwg.se.rummikub.controller.Controller

import de.htwg.se.rummikub.model._

case class TokenPanel(token: Token, controller: Controller) extends BorderPanel {
  preferredSize = new Dimension(40, 60)
  background = new java.awt.Color(245, 245, 220)
  opaque = true
  border = Swing.LineBorder(java.awt.Color.BLACK)

  val color = token match {
    case NumToken(_, c) => c match {
      case Color.RED => java.awt.Color.RED
      case Color.BLUE => java.awt.Color.BLUE
      case Color.GREEN => java.awt.Color(0, 153, 0)
      case Color.BLACK => java.awt.Color.BLACK
    }

    case Joker(c) => c match {
      case Color.RED => java.awt.Color.RED
      case Color.BLACK => java.awt.Color.BLACK
      case _ => java.awt.Color.YELLOW
    }
  }

  val label = new Label(token match {
    case NumToken(value, _) => value.toString
    case Joker(_) => "J"
  }) {
    foreground = color
    font = new Font("Arial", Font.BOLD, 16)
    horizontalAlignment = Alignment.Center
    verticalAlignment = Alignment.Center
  }

  layout(label) = BorderPanel.Position.Center
}