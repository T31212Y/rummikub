package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer
import scala.swing._
import scala.swing.event._

class GuiSwing(controller: Controller) extends MainFrame with GameView(controller) with Observer {

  title = "Rummikub GUI"
  preferredSize = new Dimension(800, 600)

  // Spielfeld-Anzeige
  val textArea = new TextArea {
    font = new Font("Monospaced", java.awt.Font.PLAIN, 14)
    editable = false
    background = new Color(245, 245, 220)
    lineWrap = false
  }

  // Hilfe-Anzeige
  val helpArea = new TextArea {
    text = showHelp
    editable = false
    background = new Color(230, 230, 250)
  }

  // Eingabefeld und Button
  val inputField = new TextField(40)
  val sendButton = new Button("Enter")

  // Panel für Eingabe
  val inputPanel = new FlowPanel {
    contents += inputField
    contents += sendButton
  }

  // Layout
  contents = new BorderPanel {
    layout(new ScrollPane(textArea)) = BorderPanel.Position.Center
    layout(inputPanel) = BorderPanel.Position.South
    layout(helpArea) = BorderPanel.Position.North
  }

  // Fenster schließen
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)

  // Observer registrieren
  controller.add(this)

  // Initialanzeige
  updateView()

  // Event-Handling
  listenTo(sendButton)
  listenTo(inputField)

  reactions += {
    case ButtonClicked(`sendButton`) | EditDone(`inputField`) =>
      handleInput()
  }

  def handleInput(): Unit = {
    val input = inputField.text.trim
    if (input.nonEmpty) {
      input match {
        case "new" =>
          createNewGame
        case "start" =>
          controller.startGame()
        case "help" =>
          Dialog.showMessage(contents.head, showHelpPage.mkString("\n"), title = "Help")
        case "quit" =>
          controller.endGame()
        case "row" =>
          val tokensOpt = Dialog.showInput(contents.head, "Tokens für Row eingeben (z.B. 1:red,2:blue,3:green):", initial = "")
          tokensOpt.foreach { tokens =>
            val tokenList = tokens.split(",").map(_.trim).toList
            val (newPlayer, message) = controller.playRow(tokenList, controller.getState.currentPlayer, controller.getState.stack)
            Dialog.showMessage(contents.head, message, title = "Row")
            val newState = controller.getState.updateCurrentPlayer(newPlayer)
            controller.setStateInternal(newState)
          }
        case "group" =>
          val tokensOpt = Dialog.showInput(contents.head, "Tokens für Group eingeben (z.B. 1:red,2:blue,3:green):", initial = "")
          tokensOpt.foreach { tokens =>
            val tokenList = tokens.split(",").map(_.trim).toList
            val (newPlayer, message) = controller.playGroup(tokenList, controller.getState.currentPlayer, controller.getState.stack)
            Dialog.showMessage(contents.head, message, title = "Group")
            val newState = controller.getState.updateCurrentPlayer(newPlayer)
            controller.setStateInternal(newState)
          }
        case _ =>
          Dialog.showMessage(contents.head, "Unknown command.", title = "Info")
      }
      inputField.text = ""
      updateView()
    }
  }

  // View-Aktualisierung
  def updateView(): Unit = {
    textArea.text = controller.playingfieldToString
    val goodbyeMsg = showGoodbye
    if (goodbyeMsg.nonEmpty) {
      Dialog.showMessage(contents.head, goodbyeMsg, title = "Goodbye")
      close()
    }
  }

  override def update: Unit = updateView()
  override def playGame: Unit = ??? // Muss noch implementiert werden
}
