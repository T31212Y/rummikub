package de.htwg.se.rummikub.model

case class Player(name: String, tokens: List[Token] = List(), commandHistory: List[String] = List(), firstMoveTokens: List[Token] = List(), hasCompletedFirstMove: Boolean = false) {

  override def toString: String = {
    s"Player: $name"
  }

  def validateFirstMove(): Boolean = {
    val structures = clusterTokens(firstMoveTokens)

    if (structures.isEmpty) {
      println(s"$name's first move is invalid: cannot form valid groups or rows")
      return false
    }

    val totalPoints = structures.map(_.points).sum

    if (totalPoints < 30) {
      println(s"$name's first move must have a total of at least 30 points. You only have $totalPoints.")
      false
    } else true
  }

  def addToFirstMoveTokens(newTokens: List[Token]): Player = {
    this.copy(firstMoveTokens = this.firstMoveTokens ++ newTokens)
  }

  def deepCopy: Player = this.copy(
    tokens = this.tokens.map(identity),
    firstMoveTokens = this.firstMoveTokens.map(identity),
    commandHistory = this.commandHistory.map(identity)
  )

  def clusterTokens(tokens: List[Token]): List[TokenStructure] = {
    
    def backtrack(remaining: List[Token], acc: List[TokenStructure]): Option[List[TokenStructure]] = {
      if (remaining.isEmpty) {
        Some(acc)
      } else {
        val minSize = 3
        val maxSize = Math.min(remaining.size, 13)

        val possibleSets = for {
          size <- minSize to maxSize
          subset <- remaining.combinations(size)
        } yield {
          val ts = subset.toList
          val group = Group(ts)
          val row = Row(ts)
          List(group, row).filter(_.isValid).map(validSet => (validSet, ts))
        }

        val flatPossibleSets = possibleSets.flatten.iterator

        flatPossibleSets.flatMap { case (set, ts) =>
          val newRemaining = remaining.diff(ts)
          backtrack(newRemaining, acc :+ set)
        }.toSeq.headOption
      }
    }

    backtrack(tokens, List()).getOrElse(List())
  }
}