package de.htwg.se.rummikub.command

import scala.util.Try

trait Command {
  def execute(): Try[Unit]
  def undo(): Try[Unit]
}
