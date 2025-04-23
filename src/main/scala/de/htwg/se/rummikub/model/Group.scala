package de.htwg.se.rummikub.model

case class Group(group: List[String]) {

  var groupTokens: List[Token | Joker] = changeStringListToTokenList(group)

  def changeStringListToTokenList(group: List[String]): List[Token | Joker] = {
    group.map { tokenString =>
      val tokenParts = tokenString.split(":")

      if (tokenParts(0) == "J") {
        tokenParts(1) match {
          case "red" => Joker(Color.RED)
          case "blue" => Joker(Color.BLUE)
          case "green" => Joker(Color.GREEN)
          case "black" => Joker(Color.BLACK)
        }
      } else {
        tokenParts(1) match {
          case "red" => Token(tokenParts(0).toInt, Color.RED)
          case "blue" => Token(tokenParts(0).toInt, Color.BLUE)
          case "green" => Token(tokenParts(0).toInt, Color.GREEN)
          case "black" => Token(tokenParts(0).toInt, Color.BLACK)
        }
      }
    }
  }

  def isValid(): Boolean = {
    if (groupTokens.isEmpty || groupTokens.size < 3) {
      return false
    }

    val firstToken = groupTokens.head
    val isDifferentColor = groupTokens.forall {
      case token: Token => token.color != firstToken.asInstanceOf[Token].color
      case joker: Joker => joker.color != firstToken.asInstanceOf[Joker].color
    }

    
    /*if (groupTokens.head.isInstanceOf[Joker]) {
      val groupTokensWithoutJoker = groupTokens.filterNot(_.isInstanceOf[Joker])
      val firstTokenWithoutJoker = groupTokensWithoutJoker.head
      val isSameNumber = groupTokensWithoutJoker.forall {
        case token: Token => token.number == firstTokenWithoutJoker.asInstanceOf[Token].number
        case _ => false
      }
      isDifferentColor && isSameNumber
    } else {
      val groupTokensWithoutJoker = groupTokens.filterNot(_.isInstanceOf[Joker])
      val isSameNumber = groupTokensWithoutJoker.forall {
        case token: Token => token.number == firstToken.asInstanceOf[Token].number
        case _ => false
      }
    }*/
      isDifferentColor
  }

  def addToken(token: Token | Joker): Unit = {
    groupTokens = groupTokens :+ token
  }

  def removeToken(token: Token): Unit = {
    groupTokens = groupTokens.filterNot(_.equals(token))
  }

  def getTokens: List[Token | Joker] = groupTokens

  override def toString: String = {
    groupTokens.map(_.toString).mkString(" ")
  }
}