package algorithms;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class MaxHeap<T extends Comparable<T>> {
    private T[] heap;
    private int size;
    private int capacity;
    private final Metrics metrics;

    @SuppressWarnings("unchecked")
    public MaxHeap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
        this.metrics = new Metrics();
        metrics.incrementMemoryAllocations((long) capacity * Long.BYTES);
    }

    @SuppressWarnings("unchecked")
    public void buildMaxHeap(T[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        this.heap = (T[]) new Comparable[arr.length];
        this.capacity = arr.length;
        this.size = arr.length;
        System.arraycopy(arr, 0, heap, 0, arr.length);
        metrics.incrementMemoryAllocations((long) arr.length * Long.BYTES);
        for (int i = size / 2 - 1; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    public void insert(T key) {
        if (size == capacity) {
            resize();
        }
        heap[size] = key;
        metrics.incrementArrayAccesses(1);
        size++;
        siftUp(size - 1);
    }

    public void increaseKey(int i, T newVal) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        metrics.incrementArrayAccesses(1);
        if (newVal.compareTo(heap[i]) < 0) {
            throw new IllegalArgumentException("New value must be greater than or equal to current");
        }
        heap[i] = newVal;
        metrics.incrementArrayAccesses(1);
        siftUp(i);
    }

    public T extractMax() {
        if (size <= 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.incrementArrayAccesses(1);
        T max = heap[0];
        metrics.incrementArrayAccesses(2);
        heap[0] = heap[size - 1];
        size--;
        if (size > 0) {
            maxHeapify(0);
        }
        return max;
    }

    public T getMax() {
        if (size <= 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.incrementArrayAccesses(1);
        return heap[0];
    }

    private void maxHeapify(int i) {
        while (true) {
            int left = left(i);
            int right = right(i);
            int largest = i;
            if (left < size) {
                metrics.incrementArrayAccesses(1);
                metrics.incrementComparisons(1);
                if (heap[left].compareTo(heap[largest]) > 0) {
                    largest = left;
                }
            }
            if (right < size) {
                metrics.incrementArrayAccesses(1);
                metrics.incrementComparisons(1);
                if (heap[right].compareTo(heap[largest]) > 0) {
                    largest = right;
                }
            }
            if (largest == i) {
                break;
            }
            swap(i, largest);
            i = largest;
        }
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = parent(i);
            metrics.incrementArrayAccesses(1);
            metrics.incrementComparisons(1);
            if (heap[parent].compareTo(heap[i]) >= 0) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        T[] newHeap = (T[]) new Comparable[capacity];
        metrics.incrementMemoryAllocations((long) capacity * Long.BYTES);
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        metrics.incrementArrayAccesses(4);
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        metrics.incrementSwaps(1);
    }

    public int getSize() {
        return size;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(heap, size));
    }

    public static class Metrics {
        private final AtomicLong comparisons = new AtomicLong();
        private final AtomicLong swaps = new AtomicLong();
        private final AtomicLong arrayAccesses = new AtomicLong();
        private final AtomicLong memoryAllocations = new AtomicLong();

        public long getComparisons() { return comparisons.get(); }
        public long getSwaps() { return swaps.get(); }
        public long getArrayAccesses() { return arrayAccesses.get(); }
        public long getMemoryAllocations() { return memoryAllocations.get(); }

        public void incrementComparisons(long value) { comparisons.addAndGet(value); }
        public void incrementSwaps(long value) { swaps.addAndGet(value); }
        public void incrementArrayAccesses(long value) { arrayAccesses.addAndGet(value); }
        public void incrementMemoryAllocations(long value) { memoryAllocations.addAndGet(value); }

        public void reset() {
            comparisons.set(0);
            swaps.set(0);
            arrayAccesses.set(0);
            memoryAllocations.set(0);
        }
    }
}
