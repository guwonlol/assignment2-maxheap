# README.md

## Max-Heap Implementation

This repository contains a Java implementation of a Max-Heap with increase-key and extract-max operations, along with performance tracking and benchmarking.

### Usage

1. Build with Maven: `mvn clean install`
2. Run tests: `mvn test`
3. Run benchmarks: `java -cp target/classes cli.BenchmarkRunner 100,1000`

### Complexity Analysis

- **Build Max-Heap**: Time Θ(n), Space O(1) auxiliary (in-place)
- **Insert**: Time O(log n), Space O(1)
- **Increase-Key**: Time O(log n), Space O(1)
- **Extract-Max**: Time O(log n), Space O(1)

For detailed analysis, see docs/analysis-report.pdf (to be added after peer review).