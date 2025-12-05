package model

case class Booking (
                      bookingId: String,
                      destinationCountry: String,
                      hotelName: String,
                      hotelRating: Double,
                      noOfPeople: Int,
                      noOfDays: Int,
                      rooms: Int,
                      bookingPrice: Double,
                      discount: Double,
                      profitMargin: Double
                    )

// Group Bookings by country - hotel name - city as one city
case class Hotel(country: String, name: String, city: String) {
  override def toString: String = s"$country - $name - $city"
}

// Scoring for each category
trait Scorable {
  def score: Double
}

// Answer for Q2 is a value
case class HotelEconomicScore(hotel: Hotel, priceScore: Double, discountScore: Double, marginScore: Double) extends Scorable {
  override def score: Double = (priceScore + discountScore + marginScore) / 3.0
}

// Answer for Q3 is a value
case class HotelProfitScore(hotel: Hotel, visitorScore: Double, marginScore: Double) extends Scorable {
  override def score: Double = (visitorScore + marginScore) / 2.0
}

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
        noOfDays = cols(13).toInt,
        rooms = cols(15).toInt,
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
