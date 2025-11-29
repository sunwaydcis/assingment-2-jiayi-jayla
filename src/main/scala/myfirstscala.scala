import scala.io.Source

case class HotelBooking(
                         bookingId: String,
                         originCountry: String,
                         hotelName: String,
                         noOfPeople: Int,
                         bookingPrice: Double,
                         discount: String,
                         profitMargin: Double
                       )

object DataHandler {

  // function to read CSV file
  def loadData(filePath: String): List[Array[String]] = {
    val bufferedSource = Source.fromFile(filePath, "Windows-1252") // specify encoding
    val data = bufferedSource.getLines().map(_.split(",")).toList
    bufferedSource.close()
    data
  }

  // Function to show first 5 rows
  def previewData(data: List[Array[String]]): Unit = {
    println("Showing first 5 rows:")
    data.take(5).foreach(row => println(row.mkString(" | ")))
  }

  // Function to count rows
  def countRows(data: List[Array[String]]): Int = {
    data.length
  }

  def main(args: Array[String]): Unit = {
    val filePath = "Hotel_Dataset.csv"

    val data = loadData(filePath)

    previewData(data)
    println(s"Total rows: ${countRows(data)}")
  }
}
