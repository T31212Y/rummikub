package de.htwg.se.rummikub.aview.gui

import scala.collection.mutable

class SelectionManager {
  private var selectedTokens: List[TokenPanel] = List()
  private var activeDragSource: Option[TokenPanel] = None

  def addToken(tp: TokenPanel): Unit = selectedTokens ::= tp
  def removeToken(tp: TokenPanel): Unit = selectedTokens = selectedTokens.filterNot(_ == tp)
  def getSelectedTokens: List[TokenPanel] = selectedTokens
  def clearSelection(): Unit = {
    selectedTokens.foreach(_.selected = false)
    selectedTokens.foreach(_.updateBorder())
    selectedTokens = List()
  }

  def setActiveDragSource(tp: TokenPanel): Unit = activeDragSource = Some(tp)
  def getActiveDragSource: Option[TokenPanel] = activeDragSource
  def clearActiveDragSource(): Unit = activeDragSource = None
}