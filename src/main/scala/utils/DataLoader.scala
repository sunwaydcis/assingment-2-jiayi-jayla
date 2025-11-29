package utils

import scala.io.{Codec, Source}
import model.Booking

import scala.util.{Failure, Success, Try, Using}

trait DataLoader {
  def loadData(filePath: String): List[Booking]
}

object CsvLoader extends DataLoader {
  override def loadData(filePath: String): List[Booking] = {
    // FIX: Explicitly use UTF-8 Codec to avoid "Input length = 1" errors on Windows
    implicit val codec: Codec = Codec.UTF8
    codec.onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE)

    val result = Using(Source.fromResource(filePath)) { source =>
      source.getLines()
        .drop(1) // Skip header
        .flatMap(Booking.fromCsv)
        .toList
    }

    result match {
      case Success(data) => data
      case Failure(exception) =>
        println(s"Failed to read file: ${exception.getMessage}")
        List.empty[Booking]
    }
  }
}