package demo

import io.circe.jawn.decode
import java.nio.file.{Files, Paths}
import munit.FunSuite

class CountScalarValuesBenchSuite extends FunSuite {
  val doc = Files.readAllBytes(Paths.get("data/1392350759849758721.json"))

  val expectedScalarCount = 72
  val expectedUrls = Set(
    "https://twitter.com/i/web/status/1392350759849758721",
    "https://t.co/pD3fUXh8pq",
    "https://meta.plasm.us/",
    "https://t.co/zQnvHk3xHo",
    "http://abs.twimg.com/images/themes/theme14/bg.gif",
    "https://abs.twimg.com/images/themes/theme14/bg.gif",
    "https://pbs.twimg.com/profile_banners/6510972/1596799512",
    "http://pbs.twimg.com/profile_images/968101001977774082/7tuuZrkA_normal.jpg",
    "https://pbs.twimg.com/profile_images/968101001977774082/7tuuZrkA_normal.jpg",
    "https://t.co/zQnvHk3xHo"
  )

  test("SerdeOperations.countScalarValues computes the correct result") {
    assertEquals(SerdeOperations.countScalarValues(doc), expectedScalarCount)
  }

  test("CirceOperations.countScalarValues computes the correct result") {
    assertEquals(CirceOperations.countScalarValues(doc), expectedScalarCount)
  }

  test("SerdeOperations.extractUrls computes the correct result") {
    assertEquals(decode[Set[String]](SerdeOperations.extractUrls(doc)), Right(expectedUrls))
  }

  test("CirceOperations.extractUrls computes the correct result") {
    assertEquals(decode[Set[String]](CirceOperations.extractUrls(doc)), Right(expectedUrls))
  }
}
