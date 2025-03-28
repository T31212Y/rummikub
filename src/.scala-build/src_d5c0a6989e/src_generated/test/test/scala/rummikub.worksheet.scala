package test.scala


final class rummikub$u002Eworksheet$_ {
def args = rummikub$u002Eworksheet_sc.args$
def scriptPath = """test/scala/rummikub.worksheet.sc"""
/*<script>*/
import scala.util.Random  
import scala.collection.mutable.ArrayBuffer

case class Player(name: String, token: List[Token | Joker]) {
  if (token.length != 14) throw new IllegalArgumentException("A player must have 14 tokens at the start")
}

case class Token(number: Int, color: String) {
  if (number < 1 || number > 13) throw new IllegalArgumentException("Number has to be between 1 and 13")
  if (color != "red" && color != "blue" && color != "green" && color != "black") throw new IllegalArgumentException("Color has to be red, blue, green or black")

  override def toString: String = s"$number $color"
}

case class Joker(color:String) {
  if (color != "red" && color != "black") throw new IllegalArgumentException("Color has to be red or black")

  override def toString(): String = s"Joker $color"
}

val tokens = List[Token | Joker] (
  Token(1, "red"), Token(2, "red"), Token(3, "red"), Token(4, "red"), Token(5, "red"), Token(6, "red"), Token(7, "red"), Token(8, "red"), Token(9, "red"), Token(10, "red"), Token(11, "red"), Token(12, "red"), Token(13, "red"),
  Token(1, "red"), Token(2, "red"), Token(3, "red"), Token(4, "red"), Token(5, "red"), Token(6, "red"), Token(7, "red"), Token(8, "red"), Token(9, "red"), Token(10, "red"), Token(11, "red"), Token(12, "red"), Token(13, "red"),
  Token(1, "blue"), Token(2, "blue"), Token(3, "blue"), Token(4, "blue"), Token(5, "blue"), Token(6, "blue"), Token(7, "blue"), Token(8, "blue"), Token(9, "blue"), Token(10, "blue"), Token(11, "blue"), Token(12, "blue"), Token(13, "blue"),
  Token(1, "blue"), Token(2, "blue"), Token(3, "blue"), Token(4, "blue"), Token(5, "blue"), Token(6, "blue"), Token(7, "blue"), Token(8, "blue"), Token(9, "blue"), Token(10, "blue"), Token(11, "blue"), Token(12, "blue"), Token(13, "blue"),
  Token(1, "green"), Token(2, "green"), Token(3, "green"), Token(4, "green"), Token(5, "green"), Token(6, "green"), Token(7, "green"), Token(8, "green"), Token(9, "green"), Token(10, "green"), Token(11, "green"), Token(12, "green"), Token(13, "green"),
  Token(1, "green"), Token(2, "green"), Token(3, "green"), Token(4, "green"), Token(5, "green"), Token(6, "green"), Token(7, "green"), Token(8, "green"), Token(9, "green"), Token(10, "green"), Token(11, "green"), Token(12, "green"), Token(13, "green"),
  Token(1, "black"), Token(2, "black"), Token(3, "black"), Token(4, "black"), Token(5, "black"), Token(6, "black"), Token(7, "black"), Token(8, "black"), Token(9, "black"), Token(10, "black"), Token(11, "black"), Token(12, "black"), Token(13, "black"),
  Token(1, "black"), Token(2, "black"), Token(3, "black"), Token(4, "black"), Token(5, "black"), Token(6, "black"), Token(7, "black"), Token(8, "black"), Token(9, "black"), Token(10, "black"), Token(11, "black"), Token(12, "black"), Token(13, "black"),
  Joker("red"), Joker("black")
)

val initTokenP1 = ArrayBuffer[Token | Joker]()
val initTokenP2 = ArrayBuffer[Token | Joker]()

for (i <- 0 to 13) {
  val rand = Random.nextInt(tokens.length)
  initTokenP1 += tokens(rand)
  tokens.drop(rand)
}

for (i <- 0 to 13) {
  val rand = Random.nextInt(tokens.length)
  initTokenP2 += tokens(rand)
  tokens.drop(rand)
}

val player1 = Player("Anki", initTokenP1.toList)
val player2 = Player("Imma", initTokenP2.toList)
/*</script>*/ /*<generated>*//*</generated>*/
}

object rummikub$u002Eworksheet_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new rummikub$u002Eworksheet$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export rummikub$u002Eworksheet_sc.script as `rummikub.worksheet`

