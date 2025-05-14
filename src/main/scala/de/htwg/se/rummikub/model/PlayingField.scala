package de.htwg.se.rummikub.model

case class PlayingField(players: List[Player], boards: List[Board], innerField: Table)





/*        amountOfPlayers: Int, 
        players: List[Player],
        cntRows: Int = 20,
        cntTokens: Int = 24,
        cntEdgeSpaces: Int = 15,
        var innerField2Players: Table = new Table(20, 80), 
        var innerField34Players: Table = new Table(20, 80)) {
 
  val player1 = players(0)
  val player2 = players(1)

  val player3 = if (amountOfPlayers > 2) players(2) else new Player("")
  val player4 = if (amountOfPlayers > 3) players(3) else new Player("")

  var boardP1 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "up")
  var boardP2 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "down")

  var boardP13 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "up")
  var boardP24 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "down")

  if (innerField2Players.tokensOnTable == List()) {
    innerField2Players = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)
  }

  if (innerField34Players.tokensOnTable == List()) {
    innerField34Players = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
  }

  def updatePlayingField(): PlayingField = {
    var pf = new PlayingField(amountOfPlayers, players)
    if (amountOfPlayers == 2) {
      pf = pf.updatePlayer1With2Players()
      pf = pf.updatePlayer2With23Players()
      pf.innerField2Players = new Table(cntRows - 4, pf.boardP1.size(pf.boardP1.wrapBoardRowSingle(pf.boardP1.boardELRP12_1)) - 2, this.innerField2Players.tokensOnTable)
    } else if (amountOfPlayers == 3) {
      pf = pf.updatePlayer1With34Players()
      pf = pf.updatePlayer3()
      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens.take(cntTokens), pf.player3.tokens.take(cntTokens))
      
      pf.boardP2.maxLen = pf.boardP13.size(pf.boardP13.wrapBoardRowDouble(pf.boardP13.boardELRP12_1, pf.boardP13.boardELRP34_1)) - pf.boardP2.size(pf.boardP2.wrapBoardRowSingle(pf.boardP2.boardELRP12_1))
      pf = pf.updatePlayer2With23Players()
      pf.innerField34Players = new Table(cntRows - 4, pf.boardP13.size(pf.boardP13.wrapBoardRowDouble(pf.boardP13.boardELRP12_1, pf.boardP13.boardELRP34_1)) - 2, this.innerField34Players.tokensOnTable)
    } else {
      pf = pf.updatePlayer1With34Players()
      pf = pf.updatePlayer3()
      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens.take(cntTokens), pf.player3.tokens.take(cntTokens))
      
      pf = pf.updatePlayer2With4Players()
      pf = pf.updatePlayer4()
      pf.boardP24.boardEUD = pf.boardP24.createBoardFrameDouble(pf.player2.tokens.take(cntTokens), pf.player4.tokens.take(cntTokens))

      pf.innerField34Players = new Table(cntRows - 4, pf.boardP24.size(pf.boardP24.wrapBoardRowDouble(pf.boardP24.boardELRP12_1, pf.boardP24.boardELRP34_1)) - 2, this.innerField34Players.tokensOnTable)
    }
    pf
  }

  def updatePlayer1With2Players(): PlayingField = {
    if (player1.tokens.size < cntTokens + 1) {
        boardP1.boardELRP12_1 = boardP1.formatBoardRow(player1.tokens)
        boardP1.boardELRP12_2 = boardP1.formatEmptyBoardRow(boardP1.size(boardP1.boardELRP12_1) - 4)
        boardP1.boardEUD = boardP1.createBoardFrameSingle(player1.tokens)
      } else {
        boardP1.boardELRP12_1 = boardP1.formatBoardRow(player1.tokens.take(cntTokens))
        boardP1.boardELRP12_2 = boardP1.formatBoardRow(player1.tokens.drop(cntTokens))
        boardP1.boardEUD = boardP1.createBoardFrameSingle(player1.tokens.take(cntTokens))
      }
      this
  }

  def updatePlayer2With23Players(): PlayingField = {
    if (player2.tokens.size < cntTokens + 1) {
        boardP2.boardELRP12_1 = boardP2.formatBoardRow(player2.tokens)
        boardP2.boardELRP12_2 = boardP2.formatEmptyBoardRow(boardP2.size(boardP2.boardELRP12_1) - 4)
        boardP2.boardEUD = boardP2.createBoardFrameSingle(player2.tokens)
      } else {
        boardP2.boardELRP12_1 = boardP2.formatBoardRow(player2.tokens.take(cntTokens))
        boardP2.boardELRP12_2 = boardP2.formatBoardRow(player2.tokens.drop(cntTokens))
        boardP2.boardEUD = boardP2.createBoardFrameSingle(player2.tokens.take(cntTokens))
      }
      this
  }

  def updatePlayer1With34Players(): PlayingField = {
    if (player1.tokens.size < cntTokens + 1) {
        boardP13.boardELRP12_1 = boardP13.formatBoardRow(player1.tokens)
        boardP13.boardELRP12_2 = boardP13.formatEmptyBoardRow(boardP13.size(boardP13.boardELRP12_1) - 4)
      } else {
        boardP13.boardELRP12_1 = boardP13.formatBoardRow(player1.tokens.take(cntTokens))
        boardP13.boardELRP12_2 = boardP13.formatBoardRow(player1.tokens.drop(cntTokens))
      }
      this
  }

  def updatePlayer3(): PlayingField = {
     if (player3.tokens.size < cntTokens + 1) {
        boardP13.boardELRP34_1 = boardP13.formatBoardRow(player3.tokens)
        boardP13.boardELRP34_2 = boardP13.formatEmptyBoardRow(boardP13.size(boardP13.boardELRP34_1) - 4)
      } else {
        boardP13.boardELRP34_1 = boardP13.formatBoardRow(player3.tokens.take(cntTokens))
        boardP13.boardELRP34_2 = boardP13.formatBoardRow(player3.tokens.drop(cntTokens))
      }
      this
  }

  def updatePlayer2With4Players(): PlayingField = {
     if (player2.tokens.size < cntTokens + 1) {
        boardP24.boardELRP12_1 = boardP24.formatBoardRow(player2.tokens)
        boardP24.boardELRP12_2 = boardP24.formatEmptyBoardRow(boardP24.size(boardP24.boardELRP12_1) - 4)
      } else {
        boardP24.boardELRP12_1 = boardP24.formatBoardRow(player2.tokens.take(cntTokens))
        boardP24.boardELRP12_2 = boardP24.formatBoardRow(player2.tokens.drop(cntTokens))
      }
      this
  }

  def updatePlayer4(): PlayingField = {
    if (player4.tokens.size < cntTokens + 1) {
        boardP24.boardELRP34_1 = boardP24.formatBoardRow(player4.tokens)
        boardP24.boardELRP34_2 = boardP24.formatEmptyBoardRow(boardP24.size(boardP24.boardELRP34_1) - 4)
      } else {
        boardP24.boardELRP34_1 = boardP24.formatBoardRow(player4.tokens.take(cntTokens))
        boardP24.boardELRP34_2 = boardP24.formatBoardRow(player4.tokens.drop(cntTokens))
      }
      this
  }

  def lineWithPlayerName(char: String, length: Int, p: String): String = {
    val len = length - p.length - 5
    char.repeat(5) + p + char * len
  }

  def lineWith2PlayerNames(char: String, length: Int, p1: String, p2: String): String = {
    val len = length - p1.length - p2.length - 10
    char.repeat(5) + p1 + char * len + p2 + char.repeat(5)
  }

  def simpleLine(char: String, length: Int): String = {
    char * length
  }

  override def toString(): String = {
    var playingField = ""
    if (amountOfPlayers == 2) { 
      val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name) + "\n"
      val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name) + "\n"

      playingField = edgeUp + boardP1.toString() + innerField2Players.toString() + boardP2.toString() + edgeDown + "\n"
    } else if (amountOfPlayers == 3) {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player2.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField34Players.toString() + boardP2.toString() + edgeDown + "\n"
    } else {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val edgeDown = lineWith2PlayerNames("*", boardP24.size(boardP24.wrapBoardRowDouble(boardP24.boardELRP12_1, boardP24.boardELRP34_1)), player2.name, player4.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField34Players.toString() + boardP24.toString() + edgeDown + "\n"
    }
    playingField
  }
}*/