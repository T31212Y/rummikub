package de.htwg.se.rummikub.model

case class Table(cntRows: Int, length: Int, tokensOnTable: List[List[Token]] = List()) {

    val emptyRow = "|" + (" " * length) + "|\n"

    def deleteColorCodes(tableRow: String): String = {
        tableRow.replaceAll("\u001B\\[[;\\d]*m", "")
    }

    def add (e: List[Token]): Table = this.copy(tokensOnTable = this.tokensOnTable :+ e)

    def remove(tokens: List[String]): Table = {
        val tokensToRemove = tokens.map { tokenString =>
            val tokenParts = tokenString.split(":")
            val tokenFactory = new StandardTokenFactory
            if (tokenParts(0) == "J") {
                tokenFactory.createJoker(tokenParts(1) match {
                    case "red" => Color.RED
                    case "black" => Color.BLACK
                })
            } else {
                tokenFactory.createNumToken(tokenParts(0).toInt, tokenParts(1) match {
                    case "red" => Color.RED
                    case "blue" => Color.BLUE
                    case "green" => Color.GREEN
                    case "black" => Color.BLACK
                })
            }
        }

        val updatedTokensOnTable = tokensOnTable.map { row =>
            row.filterNot(token => tokensToRemove.contains(token))
        }.filter(_.nonEmpty)

        this.copy(tokensOnTable = updatedTokensOnTable)
    }

    override def toString: String = {
        if (tokensOnTable.isEmpty) {
            emptyRow.repeat(cntRows)
        } else {
            val formattedRows = tokensOnTable.map { row =>
                val rawRow = row.map(_.toString).mkString(" ")
                val rawRowLength = deleteColorCodes(rawRow).length
                val padding = math.max(0, length - rawRowLength - 1)
                val formattedRow = " " + rawRow + " " * padding
                "|" + formattedRow + "|"
            }.mkString("\n")

            if (tokensOnTable.size < cntRows) {
                val emptyRows = cntRows - tokensOnTable.size
                formattedRows + "\n" + emptyRow.repeat(emptyRows)
            } else {
                formattedRows
            }
        }
    }
}