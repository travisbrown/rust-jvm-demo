package demo

import io.circe.{Json, JsonNumber, JsonObject}
import io.circe.jawn.parseByteArray
import io.circe.syntax._

object CirceOperations extends Operations {
  def countScalarValues(input: Array[Byte]): Int = countScalarValuesFromJson(parseByteArray(input).right.get)
  def extractUrls(input: Array[Byte]): String = extractUrlsFromJson(parseByteArray(input).right.get).asJson.noSpaces

  private def countScalarValuesFromJson(value: Json): Int = value.fold(
    1,
    _ => 1,
    _ => 1,
    _ => 1,
    _.map(countScalarValuesFromJson).sum,
    _.values.map(countScalarValuesFromJson).sum
  )

  private def extractUrlsFromJson(value: Json): Vector[String] = value.fold(
    Vector.empty,
    _ => Vector.empty,
    _ => Vector.empty,
    s => if (s.startsWith("http")) Vector(s) else Vector.empty,
    _.flatMap(extractUrlsFromJson),
    _.values.flatMap(extractUrlsFromJson).toVector
  )

  //def countScalarValues(input: Array[Byte]): Int = parseByteArray(input).right.get.foldWith(CountScalarValues)
  //def extractUrls(input: Array[Byte]): String = parseByteArray(input).right.get.foldWith(ExtractUrls).asJson.noSpaces

  /*
  private object CountScalarValues extends Json.Folder[Int] {
    def onNull: Int = 1
    def onBoolean(value: Boolean): Int = 1
    def onNumber(value: JsonNumber): Int = 1
    def onString(value: String): Int = 1
    def onArray(value: Vector[Json]): Int = value.map(_.foldWith(this)).sum
    def onObject(value: JsonObject): Int = value.values.map(_.foldWith(this)).sum
  }
  private object ExtractUrls extends Json.Folder[Vector[String]] {
    def onNull: Vector[String] = Vector.empty
    def onBoolean(value: Boolean): Vector[String] = Vector.empty
    def onNumber(value: JsonNumber): Vector[String] = Vector.empty
    def onString(value: String): Vector[String] = if (value.startsWith("http")) Vector(value) else Vector.empty
    def onArray(value: Vector[Json]): Vector[String] = value.flatMap(_.foldWith(this))
    def onObject(value: JsonObject): Vector[String] = value.values.flatMap(_.foldWith(this)).toVector
  }*/
}
