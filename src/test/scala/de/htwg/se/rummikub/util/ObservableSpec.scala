package de.htwg.se.rummikub.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ObservableSpec extends AnyWordSpec with Matchers {

  "An Observable" should {
    "notify all observers when notified" in {
      val observable = new Observable
      var notified = false

      val observer = new Observer {
        override def update: Unit = notified = true
      }

      observable.add(observer)
      observable.notifyObservers

      notified should be(true)
    }

    "not notify removed observers" in {
      val observable = new Observable
      var notified = false

      val observer = new Observer {
        override def update: Unit = notified = true
      }

      observable.add(observer)
      observable.remove(observer)
      observable.notifyObservers

      notified should be(false)
    }
  }
}

