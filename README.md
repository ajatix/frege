# frege

### usage
```sbt
libraryDependencies ++= Seq(
  "ninja.scala" %% "frege-core" % "0.1.0",
  "ninja.scala" %% "frege-testkit" % "0.1.0" % Test
)
```

### running benchmarks
```
sbt clean benchmarks/"jmh:run -i 3 -wi 3 -f1 -t1 .*EvaluatorBench.*"
```
To see the complete [benchmark results](BENCHMARKS.md)
