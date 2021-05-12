package demo

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

    val result = LazyList
      .unfold(buffer)(b =>
        if (b.hasRemaining()) {
          val c = b.get() & 0xff
          if (c > 0) Some((c.toChar, b)) else None
        } else None
      )
      .mkString

    this.wasmImpl.dealloc(resultOffset, buffer.position() - resultOffset)
    result
  }
}

object SerdeOperations extends SerdeOperations(20 * 65536)
