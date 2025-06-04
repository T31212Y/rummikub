package de.htwg.se.rummikub.util.commandComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class UndoManagerSpec extends AnyWordSpec {
  "An UndoManager" should {
    "doStep should call doStep on the command and add it to the undo stack" in {
      var called = false
      val cmd = new Command {
        override def doStep(): Unit = called = true
        override def undoStep(): Unit = ()
        override def redoStep(): Unit = ()
      }
      val manager = new UndoManager
      manager.doStep(cmd)
      called shouldBe true
    }

    "undoStep should call undoStep on the last command and move it to the redo stack" in {
      var undoCalled = false
      val cmd = new Command {
        override def doStep(): Unit = ()
        override def undoStep(): Unit = undoCalled = true
        override def redoStep(): Unit = ()
      }
      val manager = new UndoManager
      manager.doStep(cmd)
      manager.undoStep()
      undoCalled shouldBe true
    }

    "redoStep should call redoStep on the last undone command and move it back to the undo stack" in {
      var redoCalled = false
      val cmd = new Command {
        override def doStep(): Unit = ()
        override def undoStep(): Unit = ()
        override def redoStep(): Unit = redoCalled = true
      }
      val manager = new UndoManager
      manager.doStep(cmd)
      manager.undoStep()
      manager.redoStep()
      redoCalled shouldBe true
    }

    "undoStep and redoStep should do nothing if stacks are empty" in {
      val manager = new UndoManager
      noException should be thrownBy manager.undoStep()
      noException should be thrownBy manager.redoStep()
    }
  }
}

