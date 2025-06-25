package de.htwg.se.rummikub.aview.gui

import scala.swing._
import java.awt.{Graphics2D, Dimension, Point => AwtPoint}

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{Joker, NumToken}

class DropZone(isRow: Boolean, controller: ControllerInterface) extends Panel {
  preferredSize = new Dimension(200, 100)
  //background = if (isRow) new java.awt.Color(220, 255, 220) else new java.awt.Color(220, 220, 255)
  //border = Swing.LineBorder(java.awt.Color.DARK_GRAY, 2)
  opaque = false

  var tokens: List[TokenInterface] = List()
  val zoneType: String = if (isRow) "row" else "group"

  private val tokenPositions = scala.collection.mutable.Map[TokenInterface, AwtPoint]()

  def addToken(token: TokenInterface, location: AwtPoint): Unit = {
    tokens = tokens :+ token
    tokenPositions(token) = location
    repaint()
  }

  def clearTokens(): Unit = {
    tokens = List()
    tokenPositions.clear()
    repaint()
  }

  def getIsRow: Boolean = isRow

  def handleDrop(tokens: List[TokenInterface]): Unit = {
    val formattedTokens = tokens.map(tokenToString)
    if (isRow) {
      val (newPlayer, message) = controller.playRow(formattedTokens, controller.getState.currentPlayer, controller.getState.currentStack)
      controller.setStateInternal(controller.getState.updateCurrentPlayer(newPlayer))
    } else {
      val (newPlayer, message) = controller.playGroup(formattedTokens, controller.getState.currentPlayer, controller.getState.currentStack)
      controller.setStateInternal(controller.getState.updateCurrentPlayer(newPlayer))
    }
    println("Dropped and processed tokens.")
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    tokens.zipWithIndex.foreach { case (token, i) =>
      val pos = tokenPositions.getOrElse(token, new AwtPoint(10 + i * 45, 30))
      val c = token match {
        case NumToken(_, col) => col match {
          case Color.RED => java.awt.Color.RED
          case Color.BLUE => java.awt.Color.BLUE
          case Color.GREEN => new java.awt.Color(0, 153, 0)
          case Color.BLACK => java.awt.Color.BLACK
        }
        case Joker(col) => col match {
            case Color.RED => java.awt.Color.RED
            case Color.BLACK => java.awt.Color.BLACK
            case _ => java.awt.Color.YELLOW
        }
      }
      g.setColor(c)
      g.fillRoundRect(pos.x, pos.y, 40, 60, 10, 10)
      g.setColor(java.awt.Color.BLACK)
      g.drawRoundRect(pos.x, pos.y, 40, 60, 10, 10)
      val label = token match {
        case NumToken(value, _) => value.toString
        case Joker(_) => "J"
      }
      g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16))
      g.drawString(label, pos.x + 15, pos.y + 35)
    }
  }

  def tokenToString(token: TokenInterface): String = token match {
    case NumToken(value, color) =>
      val colorChar = color match {
        case Color.RED => "red"
        case Color.BLUE => "blue"
        case Color.GREEN => "green"
        case Color.BLACK => "black"
      }
      s"$value:$colorChar"
    case Joker(color) =>
      val colorChar = color match {
        case Color.RED => "red"
        case Color.BLACK => "black"
        case _ => "undefined"
      }
      s"J:$colorChar"
  }
}