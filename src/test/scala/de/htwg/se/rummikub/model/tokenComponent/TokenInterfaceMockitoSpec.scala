package de.htwg.se.rummikub.model.tokenComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

class TokenInterfaceMockitoSpec extends AnyWordSpec with Matchers with MockitoSugar {
  "A TokenInterface mock" should {
    "return predefined values" in {
      val mockToken = mock[TokenInterface]

      when(mockToken.getNumber).thenReturn(Some(8))
      when(mockToken.getColor).thenReturn(Color.RED)

      mockToken.getNumber shouldBe Some(8)
      mockToken.getColor shouldBe Color.RED
    }

    "verify method interactions" in {
      val mockToken = mock[TokenInterface]

      when(mockToken.getNumber).thenReturn(Some(4))

      mockToken.getNumber
      mockToken.getNumber

      verify(mockToken, times(2)).getNumber
      verify(mockToken, never()).getColor
    }
  }
}