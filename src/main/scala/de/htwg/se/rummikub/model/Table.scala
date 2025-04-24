package de.htwg.se.rummikub.model

case class Table(cntRows: Int, length: Int, tokensOnTable: List[List[Token | Joker]] = List()) {

    val emptyRow = "|" + (" " * length) + "|\n"

    def deleteColorCodes(tableRow: String): String = {
        tableRow.replaceAll("\u001B\\[[;\\d]*m", "")
    }

    def add (e: List[Token | Joker]): Table = this.copy(tokensOnTable = this.tokensOnTable :+ e)

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