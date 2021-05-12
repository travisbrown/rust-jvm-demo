package demo

import java.nio.file.{Files, Paths}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class CountScalarValuesBench {
  val doc = Files.readAllBytes(Paths.get("data/1392350759849758721.json"))

  @Benchmark
  def circe: Int = CirceOperations.countScalarValues(doc)

  @Benchmark
  def serde: Int = SerdeOperations.countScalarValues(doc)
}
