package model

case class Booking (
                      bookingId: String,
                      originCountry: String,
                      hotelName: String,
                      hotelRating: Double,
                      noOfPeople: Int,
                      bookingPrice: Double,
                      discount: Double,
                      profitMargin: Double
                    )

object Booking {
//  Method to create a HotelBooking from csv
  def fromCsv(line: String): Option[Booking] = {
    try {
      val cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1).map(_.trim)

      // Helper to clean currency/percentage strings
      def parsePercentage(s: String): Double = s.replace("%", "").toDouble / 100.0
      
      Some(Booking(
        bookingId = cols(0),
        originCountry = cols(9),
        hotelName = cols(16),
        hotelRating = cols(17).toDouble,
        noOfPeople = cols(11).toInt,
        bookingPrice = cols(20).toDouble,
        discount = parsePercentage(cols(21)),
        profitMargin = cols(23).toDouble
      ))
    } catch {
      case e: Exception =>
        println(s"Error parsing line: $line. Error: ${e.getMessage}")
        None    
    }
  }
}
