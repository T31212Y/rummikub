package de.htwg.se.rummikub.model
 
package de.htwg.se.rummikub.model

case class Row(tokens: List[Token]) {
    def getTokens: List[Token] = tokens
    def addToken(token: Token): Row = copy(tokens = tokens :+ token)
    def removeToken(token: Token): Row = copy(tokens = tokens.filterNot(_ == token))
    override def toString: String = tokens.map(_.toString).mkString(" ")

    def isValid: Boolean = {
            if (tokens.size < 3 || tokens.size > 13) return false

            val (jokers, nonJokers) = tokens.partition(_.isInstanceOf[Joker])
            val numTokens = nonJokers.collect {
                case NumToken(n, c) => (n, c) 
            }

            if (numTokens.isEmpty) return false
            if (!hasUniformColor(numTokens)) return false

            val nums = numTokens.map(_._1).distinct.sorted
            val totalLength = nums.length + jokers.length

            hasValidSequenceWithJokers(nums, jokers.length, totalLength)
        }

        private def hasUniformColor(tokens: List[(Int, Color)]): Boolean = {
            tokens.map(_._2).distinct.size == 1
        }

        private def generateCyclicSequence(start: Int, length: Int): List[Int] = {
            LazyList.iterate(start)(_ % 13 + 1).take(length).toList
        }

        private def countMissingValues(targetSeq: List[Int], actualValues: List[Int]): Int = {
            val missing = targetSeq.diff(actualValues)
            if ((actualValues.toSet & missing.toSet).nonEmpty) Int.MaxValue else missing.size
        }

        private def hasValidSequenceWithJokers(values: List[Int], jokers: Int, totalLength: Int): Boolean = {
            (1 to 13).exists { start =>
            val sequence = generateCyclicSequence(start, totalLength)
            val neededJokers = countMissingValues(sequence, values)
            neededJokers <= jokers
            }
        }

        def jokerValues: Option[List[Int]] = {
            val (jokers, nonJokers) = tokens.partition(_.isInstanceOf[Joker])
            val numTokens = nonJokers.collect { case NumToken(n, _) => n }.sorted

            if (numTokens.isEmpty) return None

            val totalLength = tokens.length

            (1 to 13).iterator.map { start =>
                val sequence = generateCyclicSequence(start, totalLength)
                val missing = sequence.diff(numTokens)
                if (missing.size == jokers.size) Some(missing)
                else None
            }.collectFirst { case Some(vals) => vals }
        }

        def points: Int = {
            jokerValues match {
            case Some(jVals) =>
                tokens.zipAll(jVals, null, 0).map {
                case (NumToken(n, _), _) => n
                case (_: Joker, jVal)    => jVal
                case _                   => 0
                }.sum
            case None =>
                tokens.collect { case NumToken(n, _) => n }.sum
            }
        }
    }

    object Row {
    def changeStringListToTokenList(tokenStrings: List[String], tokenFactory: StandardTokenFactory): List[Token] = {
        tokenStrings.map {
        case s"$num:red"   => tokenFactory.createNumToken(num.toInt, Color.RED)
        case s"$num:blue"  => tokenFactory.createNumToken(num.toInt, Color.BLUE)
        case s"$num:green" => tokenFactory.createNumToken(num.toInt, Color.GREEN)
        case s"$num:black" => tokenFactory.createNumToken(num.toInt, Color.BLACK)
        case _ => throw new IllegalArgumentException("Invalid token string")
        }
    }
}

        