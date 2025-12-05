package service

import model.*

import scala.language.postfixOps
import scala.util.Try

class HotelAnalysis(bookings: List[Booking]) {
  
  // Grouping Hotels by Country-Name-City
  private def groupHotels(bookings: List[Booking]): Map[Hotel, List[Booking]] = {
    bookings.groupBy { b =>
      Hotel(b.destinationCountry, b.hotelName, b.destinationCity)
    }
  }
  
  // Normalisation when high score = high value 
  private def normalize(value: Double, min: Double, max: Double): Double = {
    if(max == min)
      100.0
    else
      ((value - min) / (max - min)) * 100
  }
  
  // Normalisation when low score = high value
  private def normalizeInverted(value: Double, min: Double, max: Double): Double = {
    if (max == min)
      100.0
    else
      1 - ((value - max) / (max - min)) * 100
  }
  
  // Helper for Average calculation to keep code clean
  private def avg(nums: List[Double]): Double =
    if (nums.isEmpty) 0.0 else nums.sum / nums.size
  
  private def getMostFrequent[T](data: List[T], extractor: T => String): (String, Int) = {
    data.groupBy(extractor)
      .map { case (key, group) => (key, group.size) }
      .maxBy(_._2)
  }

  // -------------------------------------------
  // Q1: Highest number of bookings
  // -------------------------------------------
  def getTopCountryWithMostBookings(): (String, Int) = {
    getMostFrequent(bookings, _.destinationCountry)
  }

  // -------------------------------------------
  // Q2: Most Economical to Customers
  // -------------------------------------------
  def getMostEconomicalHotel(bookings: List[Booking]): Option[(HotelEconomicScore, Double)] = {
    val grouped = groupHotels(bookings)

    val rawStats = grouped.map { case (key, records) =>
      val avgPrice = records.map(_.bookingPrice).sum / records.size
      val avgDiscount = records.map(_.discount).sum / records.size
      val avgMargin = records.map(_.profitMargin).sum / records.size
      (key, avgPrice, avgDiscount, avgMargin)
    }.toList

    if (rawStats.isEmpty) return None

    val prices = rawStats.map(_._2)
    val discounts = rawStats.map(_._3)
    val margins = rawStats.map(_._4)

    val (minP, maxP) = (prices.min, prices.max)
    val (minD, maxD) = (discounts.min, discounts.max)
    val (minM, maxM) = (margins.min, margins.max)

    val scores = rawStats.map { case (key, p, d, m) =>
      HotelEconomicScore(
        hotel = key,
        priceScore = normalizeInverted(p, minP, maxP),
        discountScore = normalize(d, minD, maxD),
        marginScore = normalizeInverted(m, minM, maxM)
      )
    }
    val bestHotel = scores.maxBy(_.score)
    Some(bestHotel, bestHotel.score)
  }

  // -------------------------------------------
  // Q3: Most Profitable Hotel 
  // -------------------------------------------
  def getMostProfitableHotel(bookings: List[Booking]): Option[(HotelProfitScore, Double)] = {
    val grouped = groupHotels(bookings)

    val rawStats = grouped.map { case (key, records) =>
      val totalVisitors = records.map(_.noOfPeople).sum.toDouble
      val avgMargin = records.map(_.profitMargin).sum / records.size
      (key, totalVisitors, avgMargin)
    }.toList

    if (rawStats.isEmpty) return None

    val visitors = rawStats.map(_._2)
    val margins = rawStats.map(_._3)

    val (minV, maxV) = (visitors.min, visitors.max)
    val (minM, maxM) = (margins.min, margins.max)

    val scores = rawStats.map { case (key, v, m) =>
      HotelProfitScore(
        hotel = key,
        visitorScore = normalize(v, minV, maxV),
        marginScore = normalize(m, minM, maxM)
      )
    }

    val bestHotel = scores.maxBy(_.score)
    Some(bestHotel, bestHotel.score)
  }
}