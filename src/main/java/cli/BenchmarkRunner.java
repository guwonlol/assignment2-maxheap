// src/main/java/cli/BenchmarkRunner.java
package cli;

import algorithms.MaxHeap;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] DEFAULT_SIZES = {100, 1000, 10000, 100000};
    private static final String CSV_FILE = "performance.csv";

    public static void main(String[] args) {
        int[] sizes = parseSizes(args);
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write("Operation,InputSize,TimeNs,MemoryUsed,Comparisons,Swaps,Accesses,Allocations\n");
            for (int n : sizes) {
                runBenchmark(n, writer);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    private static int[] parseSizes(String[] args) {
        if (args.length == 0) return DEFAULT_SIZES;
        String[] parts = args[0].split(",");
        int[] sizes = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            sizes[i] = Integer.parseInt(parts[i].trim());
        }
        return sizes;
    }

    private static void runBenchmark(int n, FileWriter writer) throws IOException {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(1000000);
        }

        PerformanceTracker tracker = new PerformanceTracker();
        MaxHeap heap = new MaxHeap(n);

        // Build heap
        tracker.startTracking();
        heap.resetMetrics();
        heap.buildMaxHeap(arr.clone()); // Clone to avoid modifying original
        tracker.stopTracking();
        writer.write(tracker.toCsv("buildMaxHeap", n, heap.getComparisons(), heap.getSwaps(),
                heap.getArrayAccesses(), heap.getMemoryAllocations()));

        // Extract-max all elements
        tracker.startTracking();
        heap.resetMetrics();
        while (heap.getSize() > 0) {
            heap.extractMax();
        }
        tracker.stopTracking();
        writer.write(tracker.toCsv("extractMaxAll", n, heap.getComparisons(), heap.getSwaps(),
                heap.getArrayAccesses(), heap.getMemoryAllocations()));

        // Increase-key example
        heap.buildMaxHeap(arr.clone()); // Rebuild
        tracker.startTracking();
        heap.resetMetrics();
        for (int i = 0; i < Math.min(100, n); i++) { // Limit to 100 operations for large n
            int idx = rand.nextInt(n);
            int newVal = heap.getMax() + rand.nextInt(1000);
            heap.increaseKey(idx, newVal);
        }
        tracker.stopTracking();
        writer.write(tracker.toCsv("increaseKey", n, heap.getComparisons(), heap.getSwaps(),
                heap.getArrayAccesses(), heap.getMemoryAllocations()));
    }
}