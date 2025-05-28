package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import javax.swing._
import javax.swing.WindowConstants
import java.awt._
import java.awt.event.{ActionEvent, ActionListener}

class GuiSwing(controller: Controller) extends JFrame("Rummikub GUI") with GameView(controller) with Observer {

  // TextArea für das Spielfeld
  val textArea = new JTextArea()
  textArea.setFont(new Font("Monospaced", Font.PLAIN, 14))
  textArea.setEditable(false)
  textArea.setLineWrap(false)
  textArea.setBackground(new Color(245, 245, 220)) // Beige für Boards

  val outputArea = new javax.swing.JTextArea(10, 40)
  outputArea.setEditable(false)
  outputArea.setText(showHelp)

  // Hilfe-Bereich
  val helpArea = new JTextArea(showHelp)
  helpArea.setEditable(false)
  helpArea.setBackground(new Color(230, 230, 250))

  // Eingabefeld und Button
  val inputField = new JTextField(40)
  val sendButton = new JButton("Enter")

  // Panel für Eingabe
  val inputPanel = new JPanel()
  inputPanel.add(inputField)
  inputPanel.add(sendButton)

  // ScrollPane für das Spielfeld
  val scrollPane = new JScrollPane(textArea)

  // Layout
  setLayout(new BorderLayout())
  add(scrollPane, BorderLayout.CENTER)
  add(inputPanel, BorderLayout.SOUTH)
  add(helpArea, BorderLayout.NORTH)
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setSize(800, 600)
  setVisible(true)

  // Observer registrieren
  controller.add(this)

  // Initialanzeige
  updateView()

  // Eingabe-Handling
  sendButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = handleInput()
  })
  inputField.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = handleInput()
  })

  def handleInput(): Unit = {
    val input = inputField.getText.trim
    if (input.nonEmpty) {
      input match {
        case "new" =>
          createNewGame
        case "start" =>
          controller.startGame()
        case "help" =>
          JOptionPane.showMessageDialog(this, showHelpPage.mkString("\n"), "Help", JOptionPane.INFORMATION_MESSAGE)
        case "quit" =>
          controller.endGame() 
        case "row" =>
          val tokens = JOptionPane.showInputDialog(this, "Tokens für Row eingeben (z.B. 1:red,2:blue,3:green):")
          if (tokens != null && tokens.nonEmpty) {
            val tokenList = tokens.split(",").map(_.trim).toList
            val (newPlayer, message) = controller.playRow(tokenList, controller.getState.currentPlayer, controller.getState.stack)
            JOptionPane.showMessageDialog(this, message, "Row", JOptionPane.INFORMATION_MESSAGE)
            val newState = controller.getState.updateCurrentPlayer(newPlayer)
            controller.setStateInternal(newState)
          }
        case "group" =>
          val tokens = JOptionPane.showInputDialog(this, "Tokens für Group eingeben (z.B. 1:red,2:blue,3:green):")
          if (tokens != null && tokens.nonEmpty) {
            val tokenList = tokens.split(",").map(_.trim).toList
            val (newPlayer, message) = controller.playGroup(tokenList, controller.getState.currentPlayer, controller.getState.stack)
            JOptionPane.showMessageDialog(this, message, "Group", JOptionPane.INFORMATION_MESSAGE)
            val newState = controller.getState.updateCurrentPlayer(newPlayer)
            controller.setStateInternal(newState)
          }
        case _ =>
          JOptionPane.showMessageDialog(this, "Unknown command.", "Info", JOptionPane.INFORMATION_MESSAGE)
      }
      inputField.setText("")
      updateView()
    }
  }


  // Observer-Update: Anzeige aktualisieren und ggf. Fenster schließen
  def updateView(): Unit = {
    textArea.setText(controller.playingfieldToString)
    val goodbyeMsg = showGoodbye
    if (goodbyeMsg.nonEmpty) {
      JOptionPane.showMessageDialog(this, goodbyeMsg, "Goodbye", JOptionPane.INFORMATION_MESSAGE)
      dispose()  // Fenster schließen
    }
  }

  override def update: Unit = updateView()
  override def playGame: Unit = ???
}
