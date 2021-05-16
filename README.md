# Rust on the JVM

[![Build status](https://img.shields.io/github/workflow/status/travisbrown/rust-jvm-demo/Continuous%20Integration.svg)](https://github.com/travisbrown/rust-jvm-demo/actions)

## Motivation

This repository is a quick self-contained experiment I put together to check the feasibility of
publishing build tools written in Rust for use in pure JVM contexts.

I'm working on a parser generator that can output (among other things) Java and Scala code
(specifically a parser for the provided grammar implemented in Java, and a safe wrapper, ScalaCheck
instances, etc. in Scala). The parser generator is implemented in Rust, but ideally I'd want to be
able to publish it as a pure JVM library and sbt plugin that wouldn't require the user to have any
Rust tooling installed or to have to worry about platform-specific issues.

## This demo

This project includes implementations of two simple operations in Rust and Scala:

* Parse an arbitrary JSON document and return all strings that look like URLs as a JSON array.
* Parse an arbitrary JSON document and count all scalar values.

The Rust implementation uses [Serde](https://docs.serde.rs/serde_json/) and the Scala implementation uses [Circe](https://github.com/circe/circe).

I've [compiled the Rust implementations into WASM](https://rustwasm.github.io/docs/book/)
and then the WASM into JVM bytecode with a modified version of [Asmble](https://github.com/cretz/asmble)
(which unfortunately isn't currently maintained). The resulting jar is available in the `lib/` directory, and is currently only 36K.

There are tests and [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmarks for the two implementations on the JVM.

These operations don't exactly match my actual use case, but they're close:
in both cases I'm parsing small documents in Rust, using "real" Rust libraries, doing some work, and generating new documents as output.

## Results

There's currently some unpleasant code on both the Rust and Scala sides that's needed to get strings back and forth,
but it's fairly generic and could probably mostly be moved into the WASM-to-JVM tooling.

Performance doesn't matter much for my actual use case, but I wanted to be sure it wasn't excessively bad,
and it looks okay (definitely better than I expected):

```
Benchmark                      Mode  Cnt      Score      Error  Units
ExtractUrlsBench.circe        thrpt   20  51351.757 ±  749.093  ops/s
ExtractUrlsBench.serde        thrpt   20  14380.148 ±   30.884  ops/s
CountScalarValuesBench.circe  thrpt   20  57325.181 ± 2662.133  ops/s
CountScalarValuesBench.serde  thrpt   20  16471.974 ±  345.556  ops/s
```

Warning: this code is terrible and this approach might not end up working at all.
