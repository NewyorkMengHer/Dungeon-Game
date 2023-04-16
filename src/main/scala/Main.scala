import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}

object Main {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\hadoop")
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession

      .builder
      .appName("hello hive")
      .config("spark.master", "local")
      .enableHiveSupport()
      .getOrCreate()
    println("created spark session")
    spark.sparkContext.setLogLevel("ERROR")

    spark.sql("DROP TABLE IF EXISTS database")
    spark.sql("create table IF NOT EXISTS database(id Int,Channel String, Uploads Int, Subscribers Int, Views Long) row format delimited fields terminated by ','");
    spark.sql("LOAD DATA LOCAL INPATH 'project1.txt' INTO TABLE database")
    spark.sql("SELECT * FROM database").show()
    spark.sql("")
  }
}
