import utils.CsvLoader
import service.HotelAnalysis

@main def runAnalysis(): Unit = {
  println("Loading Data...")

  // Load Data
  val bookings = CsvLoader.loadData("Hotel_Dataset.csv")
  println(s"Successfully loaded ${bookings.size} records.\n")
  println("--- Hotel Booking Analysis System ---\n")

  val analyzer = new HotelAnalysis(bookings)

  // Answer Q1
  val (topCountry, count) = analyzer.getTopCountryWithMostBookings()
  println(s"1. Country with highest bookings: $topCountry ($count bookings)")

  // Answer Q2
  println("\n2. Most economical options:")
  val (hotelPrice, avgPrice) = analyzer.getMostEconomicalByPrice()
  println(f"   a. By Booking Price: $hotelPrice (Avg: $$${avgPrice}%.2f)")

  val (hotelDiscount, avgDisc) = analyzer.getMostEconomicalByDiscount()
  println(f"   b. By Discount: $hotelDiscount (Avg: ${avgDisc * 100}%.2f%%)")

  val (hotelMargin, avgMargin) = analyzer.getMostEconomicalByMargin()
  println(f"   c. By Profit Margin: $hotelMargin (Avg Margin: ${avgMargin}%.2f)")

  // Answer Q3
  val (profitableHotel, totalProfit, totalVisitors) = analyzer.getMostProfitableHotel()
  println(f"\n3. Most profitable hotel: \n   $profitableHotel \n   Total Profit: $$${totalProfit}%.2f \n   Total Visitors: $totalVisitors")
}