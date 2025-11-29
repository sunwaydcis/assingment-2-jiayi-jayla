package service

import model.Booking

class HotelAnalysis(bookings: List[Booking]) {

  private def analyzeGroups[K](
                                grouper: Booking => K,
                                aggregator: List[Booking] => Double,
                                selector: Iterable[(K, Double)] => (K, Double)
                              ): (K, Double) = {
    val grouped = bookings.groupBy(grouper)
    val scored = grouped.map { case (key, list) =>
      (key, aggregator(list))
    }
    selector(scored)
  }

  // Helper for Average calculation to keep code clean
  private def avg(nums: List[Double]): Double =
    if (nums.isEmpty) 0.0 else nums.sum / nums.size

  private def getMostFrequent[T](data: List[T], extractor: T => String): (String, Int) = {
    data.groupBy(extractor)
      .map { case (key, group) => (key, group.size) }
      .maxBy(_._2)
  }

  // Q1: Highest number of bookings
  def getTopCountryWithMostBookings(): (String, Int) = {
    getMostFrequent(bookings, _.originCountry)
  }

  // Q2a: Most economical hotel (by Booking Price)
  def getMostEconomicalByPrice(): (String, Double) = {
    analyzeGroups(
      grouper = _.hotelName,
      aggregator = list => avg(list.map(_.bookingPrice)),
      selector = _.minBy(_._2)
    )
  }

  // Q2b: Most economical hotel (by Highest Discount)
  def getMostEconomicalByDiscount(): (String, Double) = {
    analyzeGroups(
      grouper = _.hotelName,
      aggregator = list => avg(list.map(_.discount)),
      selector = _.maxBy(_._2)
    )
  }

  // Q2c: Most economical hotel (by Lowest Profit Margin)
  def getMostEconomicalByMargin(): (String, Double) = {
    analyzeGroups(
      grouper = _.hotelName,
      aggregator = list => avg(list.map(_.profitMargin)),
      selector = _.minBy(_._2)
    )
  }

}