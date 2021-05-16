package demo

import java.nio.charset.StandardCharsets

class SerdeOperations(maxMemory: Int) extends Operations {
  private val wasmImpl = new Wasm(maxMemory)

  def countScalarValues(input: Array[Byte]): Int = {
    val offset = this.wasmImpl.alloc(input.length)
    val buffer = this.wasmImpl.getMemory

    buffer.position(offset)
    buffer.put(input)

    wasmImpl.count_scalar_values(offset, input.length)
  }

  def extractUrls(input: Array[Byte]): String = {
    val offset = this.wasmImpl.alloc(input.length)
    val buffer = this.wasmImpl.getMemory

    buffer.position(offset)
    buffer.put(input)

    val resultOffset = wasmImpl.extract_urls(offset, input.length)

    buffer.position(resultOffset)
    val slice = buffer.slice()
    var i = 0

    while (buffer.hasRemaining() && buffer.get() != 0x00) {
      i += 1
    }

    slice.limit(i)
    val result = StandardCharsets.UTF_8.decode(slice).toString

    this.wasmImpl.dealloc(resultOffset, buffer.position() - resultOffset)
    result
  }
}

object SerdeOperations extends SerdeOperations(18 * 65536)
