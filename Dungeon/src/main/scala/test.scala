import scala.io.Source.fromFile

object test {


  def main(args: Array[String]): Unit = {


    val filePath = "src/test/scala/game.json"
    val jsonString = fromFile(filePath).mkString.replace("\"", "").substring(15, 70).split(",")
    val jsonList = jsonString.toList

    println(jsonList)
  }
}