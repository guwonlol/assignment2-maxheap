// src/main/java/metrics/PerformanceTracker.java

package metrics;

/**
 * Utility class for tracking performance metrics.
 * Can be extended or used to export to CSV.
 */
public class PerformanceTracker {
    private long timeStart;
    private long timeEnd;
    private long memoryStart;
    private long memoryEnd;

    public void startTracking() {
        timeStart = System.nanoTime();
        memoryStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public void stopTracking() {
        timeEnd = System.nanoTime();
        memoryEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public long getExecutionTimeNs() {
        return timeEnd - timeStart;
    }

    public long getMemoryUsed() {
        return memoryEnd - memoryStart;
    }

    // TODO: Add CSV export method
    public String toCsv(String operation, int inputSize, long comparisons, long swaps, long accesses, long allocations) {
        return String.format("%s,%d,%d,%d,%d,%d,%d,%d\n",
                operation, inputSize, getExecutionTimeNs(), getMemoryUsed(),
                comparisons, swaps, accesses, allocations);
    }
}