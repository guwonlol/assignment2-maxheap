package cli;

import algorithms.MaxHeap;
import metrics.PerformanceTracker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class BenchmarkRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private Integer[] randomArray;
    private Integer[] sortedArray;
    private Integer[] reverseSortedArray;
    private Integer[] nearlySortedArray;
    private MaxHeap<Integer> heap;
    private PerformanceTracker tracker;
    private Random rand;

    @Param({"100", "1000", "10000", "100000"})
    private int size;

    @Setup(Level.Trial)
    public void setup() {
        rand = new Random(42);
        randomArray = new Integer[size];
        sortedArray = new Integer[size];
        reverseSortedArray = new Integer[size];
        nearlySortedArray = new Integer[size];

        // Random array
        for (int i = 0; i < size; i++) {
            randomArray[i] = rand.nextInt(1000000);
        }

        // Sorted array
        sortedArray = randomArray.clone();
        Arrays.sort(sortedArray);

        // Reverse sorted array
        for (int i = 0; i < size; i++) {
            reverseSortedArray[i] = sortedArray[size - 1 - i];
        }

        // Nearly sorted array (small perturbations)
        nearlySortedArray = sortedArray.clone();
        for (int i = 0; i < size / 10; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            Integer temp = nearlySortedArray[idx1];
            nearlySortedArray[idx1] = nearlySortedArray[idx2];
            nearlySortedArray[idx2] = temp;
        }

        heap = new MaxHeap<>(size);
        tracker = new PerformanceTracker();
    }

    @Benchmark
    public void benchmarkBuildMaxHeapRandom(Blackhole blackhole) {
        heap.getMetrics().reset();
        tracker.startTracking();
        heap.buildMaxHeap(randomArray.clone());
        tracker.stopTracking();
        blackhole.consume(heap);
        recordMetrics("buildMaxHeapRandom", size, tracker, heap.getMetrics());
    }

    @Benchmark
    public void benchmarkBuildMaxHeapSorted(Blackhole blackhole) {
        heap.getMetrics().reset();
        tracker.startTracking();
        heap.buildMaxHeap(sortedArray.clone());
        tracker.stopTracking();
        blackhole.consume(heap);
        recordMetrics("buildMaxHeapSorted", size, tracker, heap.getMetrics());
    }

    @Benchmark
    public void benchmarkBuildMaxHeapReverseSorted(Blackhole blackhole) {
        heap.getMetrics().reset();
        tracker.startTracking();
        heap.buildMaxHeap(reverseSortedArray.clone());
        tracker.stopTracking();
        blackhole.consume(heap);
        recordMetrics("buildMaxHeapReverseSorted", size, tracker, heap.getMetrics());
    }

    @Benchmark
    public void benchmarkBuildMaxHeapNearlySorted(Blackhole blackhole) {
        heap.getMetrics().reset();
        tracker.startTracking();
        heap.buildMaxHeap(nearlySortedArray.clone());
        tracker.stopTracking();
        blackhole.consume(heap);
        recordMetrics("buildMaxHeapNearlySorted", size, tracker, heap.getMetrics());
    }

    @Benchmark
    public void benchmarkExtractMaxAll(Blackhole blackhole) {
        heap.buildMaxHeap(randomArray.clone());
        heap.getMetrics().reset();
        tracker.startTracking();
        while (heap.getSize() > 0) {
            blackhole.consume(heap.extractMax());
        }
        tracker.stopTracking();
        recordMetrics("extractMaxAll", size, tracker, heap.getMetrics());
    }

    @Benchmark
    public void benchmarkIncreaseKey(Blackhole blackhole) {
        heap.buildMaxHeap(randomArray.clone());
        heap.getMetrics().reset();
        tracker.startTracking();
        for (int i = 0; i < Math.min(100, size); i++) {
            int idx = rand.nextInt(size);
            int newVal = heap.getMax() + rand.nextInt(1000);
            heap.increaseKey(idx, newVal);
        }
        tracker.stopTracking();
        blackhole.consume(heap);
        recordMetrics("increaseKey", size, tracker, heap.getMetrics());
    }

    private void recordMetrics(String operation, int inputSize, PerformanceTracker tracker, MaxHeap.Metrics metrics) {
        try (java.io.FileWriter writer = new java.io.FileWriter("performance.csv", true)) {
            if (inputSize == SIZES[0] && operation.equals("buildMaxHeapRandom")) {
                writer.write("Operation,InputSize,TimeNs,MemoryUsed,Comparisons,Swaps,Accesses,Allocations\n");
            }
            writer.write(tracker.toCsv(operation, inputSize, metrics.getComparisons(), metrics.getSwaps(),
                    metrics.getArrayAccesses(), metrics.getMemoryAllocations()));
        } catch (java.io.IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchmarkRunner.class.getSimpleName())
                .resultFormat(ResultFormatType.CSV)
                .result("jmh-results.csv")
                .build();
        new Runner(opt).run();
    }
}
