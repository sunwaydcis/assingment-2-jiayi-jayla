import utils.CsvLoader
import service.HotelAnalysis

@main def runAnalysis(): Unit = {
  println("Loading Data...")

  // Load Data
  val bookings = CsvLoader.loadData("Hotel_Dataset.csv")
  println(s"Successfully loaded ${bookings.size} records.\n")
  println("\n==================================================================")
  println("                   Hotel Booking Analysis System                    ")
  println("==================================================================\n")


  val analyzer = new HotelAnalysis(bookings)

  // ==========================================
  // EXECUTION OF QUESTION 1 (Loading Data)
  // ==========================================
  val (topCountry, count) = analyzer.getTopCountryWithMostBookings()
  println("--- Question 1: Country with highest bookings ---")
  println(s"Top Country: $topCountry ($count bookings)")

  // ==========================================
  // EXECUTION OF QUESTION 2
  // ==========================================
  println("\n--- Question 2: Best Value Hotel Analysis ---")
  analyzer.getMostEconomicalHotel(bookings) match {
    case Some((scoreObj, finalScore)) =>
      println(s"Top Hotel: ${scoreObj.hotel.toString}")
      println(f"   Price Score:    ${scoreObj.priceScore}%.2f")
      println(f"   Discount Score: ${scoreObj.discountScore}%.2f")
      println(f"   Margin Score:   ${scoreObj.marginScore}%.2f")
      println(f"   FINAL SCORE:    $finalScore%.2f")
    case None => println("No data to analyze.")
  }

  // ==========================================
  // EXECUTION OF QUESTION 3
  // ==========================================
  println("\n--- Question 3: Popularity & Performance Analysis ---")
  analyzer.getMostProfitableHotel(bookings) match {
    case Some((scoreObj, finalScore)) =>
      println(s"Top Hotel: ${scoreObj.hotel.toString}")
      println(f"   Visitor Volume Score: ${scoreObj.visitorScore}%.2f")
      println(f"   Margin Score:         ${scoreObj.marginScore}%.2f")
      println(f"   FINAL SCORE:          $finalScore%.2f")
    case None => println("No data to analyze.")
  }

  println("\n==================================================================\n")
}