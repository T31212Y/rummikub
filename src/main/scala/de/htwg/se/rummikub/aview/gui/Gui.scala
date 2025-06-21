package de.htwg.se.rummikub.aview.gui

import de.htwg.se.rummikub.aview.GameView
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent, GameStateInterface}

import scala.swing._
import scala.swing.event._
import javax.swing.BorderFactory
import javax.swing.border.TitledBorder
import javax.swing.ImageIcon
import de.htwg.se.rummikub.model.tokenComponent.Color

class Gui(controller: ControllerInterface) extends Frame with Reactor with GameView(controller) {

  listenTo(controller)
  reactions += {
    case _: UpdateEvent =>
      update
  }

  title = "Rummikub GUI"
  preferredSize = new Dimension(1200, 700)
  minimumSize = new Dimension(1000, 600)

  val stateLabel = new Label("Welcome to Rummikub!") {
    foreground = java.awt.Color.BLACK
    background = java.awt.Color.WHITE
    font = new Font("Arial", java.awt.Font.BOLD, 14)
    opaque = true 
  }

  val tokenStackSizeLabel = new Label(s"Remaining tokens in stack: ${controller.getState.currentStack.size}") {
    foreground = java.awt.Color.WHITE
    font = new Font("Arial", java.awt.Font.BOLD, 14)
  }

  val finalRoundsLabel = new Label(s"Final rounds left: ${controller.getState.getFinalRoundsLeft.getOrElse("")}") {
    foreground = java.awt.Color.WHITE
    font = new Font("Arial", java.awt.Font.BOLD, 14)
    visible = false
  }

  val drawButton = new Button("draw")
  val passButton = new Button("pass")
  val rowButton = new Button("row")
  val groupButton = new Button("group")

  val appendToRowButton = new Button("appendToRow")
  val appendToGroupButton = new Button("appendToGroup")

  val undoButton = new Button("undo")
  val redoButton = new Button("redo")

  val putInStorageButton = new Button("Place in Storage")
  val fromStorageToTableButton = new Button("fromStorageToTable")

  for (btn <- Seq(drawButton, passButton, rowButton, groupButton, appendToRowButton, appendToGroupButton, undoButton, redoButton, putInStorageButton, fromStorageToTableButton)) {
    btn.font = new Font("Arial", java.awt.Font.BOLD, 12)
    btn.background = java.awt.Color.WHITE
    btn.foreground = java.awt.Color.BLACK
  }

  val borderTitleBoard = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE), "player ")
  borderTitleBoard.setTitleColor(java.awt.Color.WHITE)

  val borderTitleTable = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE), "playing field ")
  borderTitleTable.setTitleColor(java.awt.Color.WHITE)

  val borderTitleAction = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE), "available actions ")
  borderTitleAction.setTitleColor(java.awt.Color.WHITE)

  val borderTitleControl = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE), "game info & actions")
  borderTitleControl.setTitleColor(java.awt.Color.WHITE)

  val playerBoardPanel = new HorizontalImagePanel("/board-bg.jpg") {
    hGap = 10
    vGap = 10
    border = borderTitleBoard
    preferredSize = new Dimension(1000, 180)
  }

  val tablePanel = new VerticalImagePanel("/playing-field-bg.jpg") {
    border = borderTitleTable
    preferredSize = new Dimension(1000, 400)
  }

  val actionsPanel = new BoxPanel(Orientation.Vertical) {
    border = borderTitleAction
    background = java.awt.Color(0, 41, 159)

    contents ++= Seq(
      createButtonRow(drawButton, passButton),
      createButtonRow(rowButton, groupButton),
      createButtonRow(appendToRowButton, appendToGroupButton),
      createButtonRow(undoButton, redoButton),
      createButtonRow(putInStorageButton, fromStorageToTableButton)
    )

    contents += Swing.VStrut(5)
  }

  val tokenStackLabelPanel = new FlowPanel(FlowPanel.Alignment.Left)(tokenStackSizeLabel) {
    background = java.awt.Color(0, 41, 159)
    hGap = 0
    vGap = 0
  }

  val finalRoundsLabelPanel = new FlowPanel(FlowPanel.Alignment.Left)(finalRoundsLabel) {
    background = java.awt.Color(0, 41, 159)
    hGap = 0
    vGap = 0
  }

  val storagePanel = new FlowPanel(FlowPanel.Alignment.Left)() {
    border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE), "Storage")
    preferredSize = new Dimension(300, 150)
    background = new java.awt.Color(245, 245, 220)
  }

  val controlPanel = new BoxPanel(Orientation.Vertical) {
    border = borderTitleControl
    background = java.awt.Color(0, 41, 159)

    contents += tokenStackLabelPanel
    contents += finalRoundsLabelPanel
    contents += Swing.VStrut(10)
    contents += storagePanel
    contents += Swing.VStrut(10)
    contents += actionsPanel
  }

  contents = new BorderPanel {
    layout(playerBoardPanel) = BorderPanel.Position.South
    layout(tablePanel) = BorderPanel.Position.Center
    layout(controlPanel) = BorderPanel.Position.East
    layout(stateLabel) = BorderPanel.Position.North

    val topPanel = new BoxPanel(Orientation.Vertical) {
      contents += stateLabel
      background = java.awt.Color.WHITE
    }

    layout(topPanel) = BorderPanel.Position.North
  }

  menuBar = new MenuBar {
    contents += new Menu("Game") {
      mnemonic = Key.G
      contents += new MenuItem(Action("new") {
        createNewGame
      })
      contents += new MenuItem(Action("start") {
        playGame
      })
      contents += new MenuItem(Action("quit") {
        sys.exit(0)
      })
    }
    contents += new Menu("Help") {
      mnemonic = Key.H
      contents += new MenuItem(Action("commands before game begins") {
        Dialog.showMessage(parent = null, showHelpPage.mkString("\n"), title = "Help Page")
      })
      contents += new MenuItem(Action("commands after game begins") {
        Dialog.showMessage(parent = null, showAvailableCommands.mkString("\n"), title = "Help Page")
      })
      contents += new MenuItem(Action("game manual") {
        showManualImages()
      })
    }
  }

  listenTo(
    drawButton,
    passButton,
    rowButton,
    groupButton,
    appendToRowButton,
    appendToGroupButton,
    undoButton,
    redoButton,
    putInStorageButton,
    fromStorageToTableButton
  )

  reactions += {
    case ButtonClicked(`drawButton`) =>
      stateLabel.text = "Drawing a token..."
      val (newState, message) = controller.drawFromStackAndPass
      stateLabel.text = message
      updatePlayerBoardTitle(newState)
      nextTurn

    case ButtonClicked(`passButton`) =>
      val currentState = controller.getState
      val tableValid = controller.getGameMode.isValidTable(currentState.getTable.getTokensOnTable)
      val storageEmpty = currentState.getStorageTokens.isEmpty
      val hasMadeMove = currentState.currentPlayer.getCommandHistory.nonEmpty

      if (!hasMadeMove) {
        stateLabel.text = "You must make a move before passing your turn."
      } else if (!tableValid) {
        stateLabel.text = "You can't pass: The table is not valid!"
      } else if (!storageEmpty) {
        stateLabel.text = "You can't pass: You still have tokens in storage!"
      } else {
        val (newState, message) = controller.passTurn(currentState, false)
        controller.setStateInternal(newState)
        println("Storage tokens after putTokenInStorage: " + newState.getStorageTokens.mkString(", "))
        stateLabel.text = message
        updatePlayerBoardTitle(newState)
        nextTurn
      }

    case ButtonClicked(`rowButton`) =>
      val input = Dialog.showInput(
        parent = null,
        message = "Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):",
        title = "Play Row",
        initial = ""
      )
      input match {
        case Some(tokenString) if tokenString.nonEmpty =>
          val tokenStrings = tokenString.split(",").map(_.trim).toList
          val (newPlayer, message) = controller.playRow(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
          controller.setStateInternal(controller.getState.updateCurrentPlayer(newPlayer))
          stateLabel.text = message
        case Some(_) =>
          stateLabel.text = "No input provided."
        case None =>
      }

    case ButtonClicked(`groupButton`) =>
      val input = Dialog.showInput(
        parent = null,
        message = "Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):",
        title = "Play Group",
        initial = ""
      )
      input match {
        case Some(tokenString) if tokenString.nonEmpty =>
          val tokenStrings = tokenString.split(",").map(_.trim).toList
          val (newPlayer, message) = controller.playGroup(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
          controller.setStateInternal(controller.getState.updateCurrentPlayer(newPlayer))
          stateLabel.text = message
        case Some(_) =>
          stateLabel.text = "No input provided."
        case None =>
      }

    case ButtonClicked(`appendToRowButton`) =>
      val input = Dialog.showInput(
        parent = null,
        message = "Enter the token to append (e.g. 'token1:color'):",
        title = "Append to Row",
        initial = ""
      )
      input match {
        case Some(token) if token.nonEmpty =>
          val indexInput = Dialog.showInput(
            parent = null,
            message = "Enter the row's index (starting with 0):",
            title = "Row Index",
            initial = ""
          )
          indexInput match {
            case Some(indexStr) =>
              val index = indexStr.trim.toInt
              val (updatedPlayer, message) = controller.appendTokenToRow(token.trim, index)
              controller.setStateInternal(controller.getState.updateCurrentPlayer(updatedPlayer))
              stateLabel.text = message
            case None =>
          }
        case Some(_) =>
          stateLabel.text = "No input provided."
        case None =>
      }

    case ButtonClicked(`appendToGroupButton`) =>
      val input = Dialog.showInput(
        parent = null,
        message = "Enter the token to append (e.g. 'token1:color'):",
        title = "Append to Group",
        initial = ""
      )
      input match {
        case Some(token) if token.nonEmpty =>
          val indexInput = Dialog.showInput(
            parent = null,
            message = "Enter the group's index (starting with 0):",
            title = "Group Index",
            initial = ""
          )
          indexInput match {
            case Some(indexStr) =>
              val index = indexStr.trim.toInt
              val (updatedPlayer, message) = controller.appendTokenToGroup(token.trim, index)
              controller.setStateInternal(controller.getState.updateCurrentPlayer(updatedPlayer))
              stateLabel.text = message
            case None =>
          }
        case Some(_) =>
          stateLabel.text = "No input provided."
        case None =>
      }

    case ButtonClicked(`undoButton`) =>
      controller.undo

    case ButtonClicked(`redoButton`) =>
      controller.redo

    case ButtonClicked(`putInStorageButton`) =>
      val displayList = controller.getFormattedTokensOnTableWithLabels
      val input = Dialog.showInput(
        parent = null,
        message = s"Tokens on Table:\n$displayList\n\nEnter the index of the token to put in Storage:",
        title = "Put in Storage",
        initial = ""
      )
      input match {
        case Some(idxStr) if idxStr.nonEmpty =>
          try {
            val tokenId = idxStr.trim.toInt
            controller.putTokenInStorage(tokenId) match {
              case Some(newState) =>
                controller.setStateInternal(newState)
                update
              case None =>
                Dialog.showMessage(null, "Ungültiger Token-Index!")
            }
          } catch {
            case _: NumberFormatException =>
              Dialog.showMessage(null, "Invalid index format!")
          }
        case Some(_) =>
          Dialog.showMessage(null, "No index entered.")
        case None =>
      }

    case ButtonClicked(`fromStorageToTableButton`) =>
      val tokenStrInput = Dialog.showInput(null, "Enter the token string (e.g. '5:red') to move from Storage:", title = "From Storage", initial = "")
      val groupIndexInput = Dialog.showInput(null, "Enter the group index:", title = "Group Index", initial = "")
      val insertAtInput = Dialog.showInput(null, "Enter the position in the group where to insert the token:", title = "Insert At", initial = "")

      (tokenStrInput, groupIndexInput, insertAtInput) match {
        case (Some(tokenStr), Some(groupStr), Some(posStr)) =>
          try {
            val groupIndex = groupStr.trim.toInt
            val insertAt = posStr.trim.toInt
            val (newState, message) = controller.fromStorageToTable(controller.getState, tokenStr.trim, groupIndex, insertAt)
            controller.setStateInternal(newState)
            stateLabel.text = message
            update
          } catch {
            case _: NumberFormatException =>
              Dialog.showMessage(null, "Invalid input! Group index and insert position must be integers.")
          }

        case _ =>
          Dialog.showMessage(null, "Operation cancelled or incomplete.")
      }
    }

    def updatePlayerBoardTitle(state: GameStateInterface): Unit = {
      val titledBorder = playerBoardPanel.border.asInstanceOf[TitledBorder]
      titledBorder.setTitle(s"${state.currentPlayer.getName} ")

      playerBoardPanel.repaint()
      playerBoardPanel.revalidate()
    }

    def updatePlayerTokens: Unit = {
      playerBoardPanel.contents.clear()

      val currentPlayer = controller.getState.currentPlayer

      for ((token, index) <- currentPlayer.getTokens.zipWithIndex) {
        val panel = TokenPanel(token, controller)
        playerBoardPanel.contents += panel
      }

      playerBoardPanel.revalidate()
      playerBoardPanel.repaint()
    }

    def updateStoragePanel(): Unit = {
      storagePanel.contents.clear()
      val storageTokens = controller.getState.getStorageTokens

      for (tokenStr <- storageTokens) {
        val token = controller.getTokenFromString(tokenStr)

        val label = new Label {
          text = token.getNumber.map(_.toString).getOrElse("J")
          foreground = token.getColor match {
            case Color.RED => java.awt.Color.RED
            case Color.BLUE => java.awt.Color.BLUE
            case Color.GREEN => java.awt.Color.GREEN
            case Color.BLACK => java.awt.Color.BLACK
          }
          font = new Font("Arial", java.awt.Font.BOLD, 16)
        }

        storagePanel.contents += label
      }

      storagePanel.revalidate()
      storagePanel.repaint()
    }


    def updateTable: Unit = {
      tablePanel.contents.clear()

      val tableTokens = controller.getState.getTable.getTokensOnTable

      val groupsPerRow = 5
      val rows = tableTokens.grouped(groupsPerRow).toSeq

      val rowsToShow = rows.take(2)

      for ((rowGroups, rowIndex) <- rowsToShow.zipWithIndex) {
        val rowPanel = new FlowPanel(FlowPanel.Alignment.Left)() {
          opaque = false
          hGap = 15
          vGap = 10
          tablePanel.preferredSize = new Dimension(1000, 250)
        }

        for ((tokenGroup, groupIndex) <- rowGroups.zipWithIndex) {
          val groupPanel = new FlowPanel(FlowPanel.Alignment.Center)() {
            opaque = false
            hGap = 4
            vGap = 2

            val borderColor = java.awt.Color.WHITE
            val borderFont = new Font("Arial", java.awt.Font.BOLD, 12)

            border = BorderFactory.createTitledBorder(
              BorderFactory.createLineBorder(borderColor),
              s"index ${rowIndex * groupsPerRow + groupIndex}",
              javax.swing.border.TitledBorder.CENTER,
              javax.swing.border.TitledBorder.BOTTOM,
              borderFont,
              borderColor
            )
          }

          tokenGroup.foreach { token =>
            groupPanel.contents += TokenPanel(token, controller)
          }

          rowPanel.contents += groupPanel
        }

        tablePanel.contents += rowPanel
        tablePanel.contents += Swing.VStrut(10)
      }

      tablePanel.revalidate()
      tablePanel.repaint()
    }

    def update: Unit = {
      val state = controller.getState

      updatePlayerTokens
      updateTable
      updateStoragePanel()

      drawButton.enabled = !state.currentStack.isEmpty

      tokenStackSizeLabel.text = s"Remaining tokens in stack: ${controller.getState.currentStack.size}"
    }

    override def playGame: Unit = {
      controller.startGame
      updatePlayerBoardTitle(controller.getState)
      update
      nextTurn
    }

    def nextTurn: Unit = {
      if (controller.winGame || controller.getGameEnded) {
        return
      }

      if (controller.getState.currentStack.isEmpty && controller.getState.getFinalRoundsLeft.isEmpty) {
        val playersRemaining = controller.getState.getPlayers.length
        controller.setStateInternal(controller.getState.updated(newPlayers = controller.getState.getPlayers, newStack = controller.getState.currentStack, newFinalRoundsLeft = Some(playersRemaining)))
        stateLabel.text = s"No tokens left in stack, final round begins! You have $playersRemaining turns left to play."

        finalRoundsLabel.visible = true
      }

      controller.getState.getFinalRoundsLeft match {
        case Some(0) =>
          finalRoundsLabel.text = "Final rounds left: 0"
          controller.setGameEnded(true)
          val winnerMessage = controller.endGame

          Dialog.showMessage(contents.head, winnerMessage, title = "Game is over!")
          stateLabel.text = "Game is over!"
          return

        case Some(n) =>
          finalRoundsLabel.text = s"Final rounds left: ${controller.getState.getFinalRoundsLeft.getOrElse("")}"
          controller.setStateInternal(controller.getState.updated(newPlayers = controller.getState.getPlayers, newStack = controller.getState.currentStack, newFinalRoundsLeft = Some(n - 1)))

        case None =>
      }

      val currentPlayer = controller.getState.currentPlayer
      val stack = controller.getState.currentStack
      controller.setPlayingField(controller.getGameMode.updatePlayingField(controller.getPlayingField))
      stateLabel.text = currentPlayer.getName + ", it's your turn!"

      controller.beginTurn(currentPlayer)

      update
    }

    def createButtonRow(button1: Button, button2: Button, width: Int = 130, height: Int = 30): FlowPanel = {
      val preferredSize = new Dimension(width, height)
      button1.preferredSize = preferredSize
      button2.preferredSize = preferredSize

      new FlowPanel(FlowPanel.Alignment.Left)(button1, button2) {
        hGap = 5
        vGap = 0
        background = new java.awt.Color(0, 41, 159)
        border = Swing.EmptyBorder(0, 0, 0, 0)
        maximumSize = new Dimension(300, height + 5)
      }
    }

    override def createNewGame: Unit = {
      stateLabel.text = "Creating a new game..."
      val input = Dialog.showInput(parent = null, message = askAmountOfPlayers, title = "Create a new game", initial = "")

        input match {
          case Some(amtStr) =>
            val amt = amtStr.trim.toInt
            val nameInput = Dialog.showInput(parent = null, message = askPlayerNames, title = "Create a new game", initial = "")

            nameInput match {
              case Some(namesStr) =>
                  val names = namesStr.split(",").map(_.trim).toList
                  controller.setupNewGame(amt, names)
                  stateLabel.text = "New game created!"
              case None =>
            }

          case None =>
            stateLabel.text = "No input provided."
        }
    }

    def showManualImages(): Unit = {
      val img1 = new Label {
        icon = new ImageIcon(getClass.getResource("/gameManualP1.jpg"))
      }

      val img2 = new Label {
        icon = new ImageIcon(getClass.getResource("/gameManualP2.jpg"))
      }

      val panel = new BoxPanel(Orientation.Vertical)
      val imgLabels = Seq(img1, img2)

      imgLabels.foreach { label =>
        panel.contents += label
        panel.contents += Swing.VStrut(10)
      }

      val scrollPane = new ScrollPane(panel) {
        preferredSize = new Dimension(1200, 700)
      }

      val helpFrame = new Frame {
        title = "Game Manual"
        contents = scrollPane
        size = new Dimension(1220, 720)
        centerOnScreen()
      }

      helpFrame.visible = true
    }

    visible = true
}