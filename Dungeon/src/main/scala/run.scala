

import scala.util.control.Breaks.{break, breakable}
import scala.io.StdIn._
import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException, SQLIntegrityConstraintViolationException, Statement}
import java.io.IOException
import scala.collection.mutable.ArrayBuffer
import scala.io.Source.fromFile

object run{
  val rnd = new scala.util.Random

    var health: Int = 300
    var attack: Int = 80
    var potions = 3

  val DRIVER = "com.mysql.cj.jdbc.Driver"
  val CONNECTION_STRING = "jdbc:mysql://localhost:3306/dungeon"
  val username = "root"
  val password = "Tseemmeejhawj7&"

  var conn:Connection = DriverManager.getConnection(CONNECTION_STRING, username, password)
  var stmt = conn.createStatement()
  val resultSet = stmt.executeQuery("SELECT * FROM weapon;")

  def monWeapon: Unit ={
    while(resultSet.next){
      println(resultSet.getString("2"))
    }
  }



def insert {
  var monstersDmg = ArrayBuffer[Int](50, 30, 80, 100, 150)
  val filePath = "src/test/scala/game.json"
  val jsonString = fromFile(filePath).mkString.replace("\"", "").substring(15, 70).split(",")
  val jsonList = jsonString.toList
  var count = 1
  val insertAbility =
    s"""
       |insert into ability (AbilityID, AbilityName, AbilityDMG, MonsterID)
       |values (?,?,?,?) """.stripMargin
  val preparedStmt: PreparedStatement = conn.prepareStatement(insertAbility)
  for (i <- 0 until jsonList.length) {
    preparedStmt.setInt(1, count)
    preparedStmt.setString(2, s"${jsonList(i)}")
    preparedStmt.setInt(3, monstersDmg.head)
    preparedStmt.setInt(4, count)
    preparedStmt.execute
    count += 1
    monstersDmg -= monstersDmg.head
  }
  preparedStmt.close
}


def executeDML(query : String) : Option[Boolean] = {
    try {
      stmt = conn.createStatement()
      Some(stmt.execute(query))
    } catch {
      case e: SQLException => {
        e.printStackTrace()
        None;
      }
      case i: IOException => {
        i.printStackTrace()
        None;
      }
      case n: SQLIntegrityConstraintViolationException => {
        n.printStackTrace()
        None;
      }
    }
  }


  def delete: Unit ={
    val preparedStmt: PreparedStatement = conn.prepareStatement("TRUNCATE ability")
    preparedStmt.execute()
    conn.close()
    preparedStmt.close
  }
def monsterReset: Unit ={
  stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 50  WHERE MonstersID= 1")
  stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 30  WHERE MonstersID= 2")
  stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 75 WHERE MonstersID= 3")
  stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 120  WHERE MonstersID= 4")
  stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 200  WHERE MonstersID= 5")
}

  def main(args: Array[String]) = {

    val monsters = ArrayBuffer("Skeleton", "Slime", "Spider", "Minotaur", "Guardian")
    val monstersHp = ArrayBuffer[Int](50, 30, 75, 120, 200)
    var monstersId = ArrayBuffer[Int](1, 2, 3, 4, 5)
    var monstersDmg = ArrayBuffer[Int](50, 30, 80, 100, 150)
    var monstersAbility = ArrayBuffer("Multi Shot", "Tackle", "Poison Spit", "Raging Bull", "Judgement")

    monsterReset

    insert

    println("Welcome to the Dungeon!")


    breakable {
      for (i <- 0 until monsters.length) {
        println("____________________________________________________________________")
        val continue = readLine("Do you wish to continue? \n1. Yes \n2. No, let's turn back \n")
        if (continue.toLong == 1) {
          println("____________________________________________________________________")
          println("You chose to continue into the dungeon")
        } else {
          println("____________________________________________________________________")
          println("You decide to turn back and leave the dungeon")
          delete

          break
        }
        println("You engaged into battle against a " + monsters.head)
        var d = monstersHp.head
        var id = monstersId.head
        var p = monstersDmg.head

        if (monsters.head == "Skeleton") {
          println("The Skeleton is equipped with a Bow. It gains +12 DMG")
          p += 12
          executeDML(s"UPDATE weapon SET TotalDMG = $p WHERE MonstersID = 1")
        }
        else if (monsters.head == "Minotaur") {
          println("The Minotaur is equipped with an Axe. It gains +20 DMG")
          p += 18
          executeDML(s"UPDATE weapon SET TotalDMG = $p WHERE MonstersID = 4")
        }
        else if (monsters.head == "Guardian") {
          println("The Guardian is equipped with a Giant Sword. It gains +30 DMG")
          p += 26
          executeDML(s"UPDATE weapon SET TotalDMG = $p WHERE MonstersID = 5")
        }


        while (d > 0) {
          println("____________________________________________________________________")
          println("Your HP: " + health)
          println(monsters.head + "'s HP: " + d)
          val move = readLine("Choose your next move \n1. Attack \n2. Use ability \n3. Heal \n4. Run \n")

          if (move.toLong == 1) {
            println("____________________________________________________________________")

            val damageDealt = rnd.nextInt(attack)
            val damageTaken = rnd.nextInt(p)
            println("You strike the " + monsters.head + " dealing " + damageDealt + " DMG")
            d -= damageDealt

            if (d > 0) {
              println("The " + monsters.head + " has " + d + " HP")
              println("The " + monsters.head + " uses its ability " + monstersAbility.head)
              health -= damageTaken
              println(s"You've taken $damageTaken DMG")


              stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = $d  WHERE MonstersID= $id")
              if (health <= 0) {
                println("____________________________________________________________________")
                println("You've taken too much damage. You quickly got out of there and ran out of the dungeon")
                delete

                break
              }
            }
            else if (d <= 0) {
              stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 0  WHERE MonstersID= $id")
            }


          }
          else if (move.toLong == 2) {
            println("____________________________________________________________________")
            println("You activated your ability! Multi Slash!")

            val multiSlash = 80
            if (rnd.nextInt(20) < 10) {
              println("...")
              Thread.sleep(1000)
              println("...")
              Thread.sleep(1000)
              println("...")
              Thread.sleep(1000)
              println("You landed a critical hit! You've hit the " + monsters.head + " for " + multiSlash + " DMG!")
              d -= multiSlash
              if (d > 0) {
                println("The " + monsters.head + " has " + d + " HP")

                stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = $d  WHERE MonstersID= $id")
              }
              else if (d <= 0) {
                stmt.executeUpdate(s"UPDATE monsters SET MonstersHP = 0  WHERE MonstersID= $id")
              }
            } else {
              println("...")
              Thread.sleep(1000)
              println("...")
              Thread.sleep(1000)
              println("...")
              Thread.sleep(1000)
              println("Your ability missed the target")
              val damageTaken = rnd.nextInt(p)
              health -= damageTaken
              println("The " + monsters.head + " uses its ability " + monstersAbility.head)
              println(s"You've taken $damageTaken DMG")
              if (health < 0) {
                println("____________________________________________________________________")
                println("You've taken too much damage. You quickly got out of there and ran out of the dungeon")
                delete

                break

              }
            }

          }
          else if (move.toLong == 3) {

            if (potions > 0 & health > 299) {
              println("____________________________________________________________________")
              println("You're still healthy. Your player doesn't need heal")

            } else if (potions > 0 & health < 300) {
              println("____________________________________________________________________")
              val heal = 100
              health += heal
              println(s"You drank a potion to heal yourself\nYou healed yourself for $heal HP \nYou have $potions potions left")
              potions -= 1
            }
            else {
              println("You've ran out of potions")
            }

          }
          else {
            println("____________________________________________________________________")
            println("You quickly ran out of the dungeon")
            delete

            break
          }


        }

        println("____________________________________________________________________")
        println("You defeated the " + monsters.head)
        monsters -= monsters.head
        monstersHp -= monstersHp.head
        monstersId -= monstersId.head
        monstersAbility -= monstersAbility.head
        monstersDmg -= monstersDmg.head
      }
      println("____________________________________________________________________")
      println("CONGRATULATIONS!")
      println(raw"You've defeated the dungeon and get the honor of having your name on the 'CHAMPION STONE'")

      val name = readLine("Type in your name: \n")
      print(name)


      delete

    }

  }
}

