# benchmarks

```
[info] Benchmark                                                    Mode  Cnt         Score          Error   Units
[info] EvaluatorBench_1_1.graphEvaluator_1_1                        avgt    3         0.530 ±        1.174   us/op
[info] EvaluatorBench_1_1.graphEvaluator_1_1                       thrpt    3         2.009 ±        1.623  ops/us
[info] EvaluatorBench_1_1.graphEvaluatorSingle_1_1                    ss    3       101.986 ±      281.042   us/op
[info] EvaluatorBench_1_1.standardEvaluator_1_1                     avgt    3         0.069 ±        0.212   us/op
[info] EvaluatorBench_1_1.standardEvaluator_1_1                    thrpt    3         7.389 ±       10.017  ops/us
[info] EvaluatorBench_1_1.standardEvaluatorSingle_1_1                 ss    3       115.835 ±      504.619   us/op

[info] EvaluatorBench_10_1.graphEvaluator_10_1                      avgt    3         1.558 ±        3.573   us/op
[info] EvaluatorBench_10_1.graphEvaluator_10_1                     thrpt    3         0.995 ±        0.162  ops/us
[info] EvaluatorBench_10_1.graphEvaluatorSingle_10_1                  ss    3       838.605 ±    21406.817   us/op
[info] EvaluatorBench_10_1.standardEvaluator_10_1                   avgt    3         0.139 ±        0.139   us/op
[info] EvaluatorBench_10_1.standardEvaluator_10_1                  thrpt    3         0.852 ±        1.520  ops/us
[info] EvaluatorBench_10_1.standardEvaluatorSingle_10_1               ss    3       313.790 ±     1142.262   us/op

[info] EvaluatorBench_10_10.graphEvaluator_10_10                    avgt    3        10.929 ±        5.631   us/op
[info] EvaluatorBench_10_10.graphEvaluator_10_10                   thrpt    3         0.151 ±        0.359  ops/us
[info] EvaluatorBench_10_10.graphEvaluatorSingle_10_10                ss    3       450.735 ±      606.199   us/op
[info] EvaluatorBench_10_10.standardEvaluator_10_10                 avgt    3         4.799 ±       21.201   us/op
[info] EvaluatorBench_10_10.standardEvaluator_10_10                thrpt    3         0.309 ±        1.080  ops/us
[info] EvaluatorBench_10_10.standardEvaluatorSingle_10_10             ss    3       994.378 ±      328.676   us/op

[info] EvaluatorBench_100_1.graphEvaluator_100_1                    avgt    3        20.027 ±        3.016   us/op
[info] EvaluatorBench_100_1.graphEvaluator_100_1                   thrpt    3         0.233 ±        0.017  ops/us
[info] EvaluatorBench_100_1.graphEvaluatorSingle_100_1                ss    3        37.981 ±      175.404   us/op
[info] EvaluatorBench_100_1.standardEvaluator_100_1                 avgt    3        32.551 ±       17.931   us/op
[info] EvaluatorBench_100_1.standardEvaluator_100_1                thrpt    3         0.025 ±        0.001  ops/us
[info] EvaluatorBench_100_1.standardEvaluatorSingle_100_1             ss    3       707.892 ±     1259.228   us/op

[info] EvaluatorBench_100_10.graphEvaluator_100_10                  avgt    3        47.081 ±       31.580   us/op
[info] EvaluatorBench_100_10.graphEvaluator_100_10                 thrpt    3         0.022 ±        0.042  ops/us
[info] EvaluatorBench_100_10.graphEvaluatorSingle_100_10              ss    3      1224.752 ±     3743.200   us/op
[info] EvaluatorBench_100_10.standardEvaluator_100_10               avgt    3      1340.514 ±      547.450   us/op
[info] EvaluatorBench_100_10.standardEvaluator_100_10              thrpt    3         0.001 ±        0.001  ops/us
[info] EvaluatorBench_100_10.standardEvaluatorSingle_100_10           ss    3      4915.205 ±    21229.784   us/op

[info] EvaluatorBench_100_100.graphEvaluator_100_100                avgt    3       651.940 ±     3759.534   us/op
[info] EvaluatorBench_100_100.graphEvaluator_100_100               thrpt    3         0.002 ±        0.001  ops/us
[info] EvaluatorBench_100_100.graphEvaluatorSingle_100_100            ss    3     18331.907 ±    32440.350   us/op
[info] EvaluatorBench_100_100.standardEvaluator_100_100             avgt    3      2300.189 ±     2505.709   us/op
[info] EvaluatorBench_100_100.standardEvaluator_100_100            thrpt    3        ≈ 10⁻⁴                 ops/us
[info] EvaluatorBench_100_100.standardEvaluatorSingle_100_100         ss    3     19162.218 ±    66999.173   us/op

[info] EvaluatorBench_1000_1.graphEvaluator_1000_1                  avgt    3        70.697 ±      386.383   us/op
[info] EvaluatorBench_1000_1.graphEvaluator_1000_1                 thrpt    3         0.017 ±        0.002  ops/us
[info] EvaluatorBench_1000_1.graphEvaluatorSingle_1000_1              ss    3       472.805 ±     2443.188   us/op
[info] EvaluatorBench_1000_1.standardEvaluator_1000_1               avgt    3       100.314 ±      102.940   us/op
[info] EvaluatorBench_1000_1.standardEvaluator_1000_1              thrpt    3        ≈ 10⁻⁴                 ops/us
[info] EvaluatorBench_1000_1.standardEvaluatorSingle_1000_1           ss    3     14749.131 ±    83409.714   us/op

[info] EvaluatorBench_1000_10.graphEvaluator_1000_10                avgt    3      1818.314 ±     1847.670   us/op
[info] EvaluatorBench_1000_10.graphEvaluator_1000_10               thrpt    3        ≈ 10⁻⁴                 ops/us
[info] EvaluatorBench_1000_10.graphEvaluatorSingle_1000_10            ss    3      6256.658 ±    28645.888   us/op
[info] EvaluatorBench_1000_10.standardEvaluator_1000_10             avgt    3    111750.414 ±   193667.885   us/op
[info] EvaluatorBench_1000_10.standardEvaluator_1000_10            thrpt    3        ≈ 10⁻⁴                 ops/us
[info] EvaluatorBench_1000_10.standardEvaluatorSingle_1000_10         ss    3    101526.082 ±   100420.980   us/op

[info] EvaluatorBench_1000_100.graphEvaluator_1000_100              avgt    3     12815.330 ±    22224.159   us/op
[info] EvaluatorBench_1000_100.graphEvaluator_1000_100             thrpt    3        ≈ 10⁻⁵                 ops/us
[info] EvaluatorBench_1000_100.graphEvaluatorSingle_1000_100          ss    3     65434.006 ±    99120.170   us/op
[info] EvaluatorBench_1000_100.standardEvaluator_1000_100           avgt    3    776037.564 ±  2076173.313   us/op
[info] EvaluatorBench_1000_100.standardEvaluator_1000_100          thrpt    3        ≈ 10⁻⁵                 ops/us
[info] EvaluatorBench_1000_100.standardEvaluatorSingle_1000_100       ss    3    874296.243 ±  1784321.973   us/op

[info] EvaluatorBench_1000_1000.graphEvaluator_1000_1000            avgt    3    239293.122 ±    76122.534   us/op
[info] EvaluatorBench_1000_1000.graphEvaluator_1000_1000           thrpt    3        ≈ 10⁻⁶                 ops/us
[info] EvaluatorBench_1000_1000.graphEvaluatorSingle_1000_1000        ss    3    598836.577 ±   595587.517   us/op
[info] EvaluatorBench_1000_1000.standardEvaluator_1000_1000         avgt    3  12531687.717 ± 46388102.022   us/op
[info] EvaluatorBench_1000_1000.standardEvaluator_1000_1000        thrpt    3        ≈ 10⁻⁵                 ops/us
[info] EvaluatorBench_1000_1000.standardEvaluatorSingle_1000_1000     ss    3    101516.262 ±   177949.272   us/op
```