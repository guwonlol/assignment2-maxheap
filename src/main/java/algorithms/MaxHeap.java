// src/main/java/algorithms/MaxHeap.java
package algorithms;

import java.util.Arrays;

public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;

    private long comparisons = 0;
    private long swaps = 0;
    private long arrayAccesses = 0;
    private long memoryAllocations = 0;

    public MaxHeap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        heap = new int[capacity];
        size = 0;
        memoryAllocations += (long) capacity * Integer.BYTES; // Approximate memory for int array
    }

    public void buildMaxHeap(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        heap = arr;
        capacity = arr.length;
        size = arr.length;
        memoryAllocations += (long) arr.length * Integer.BYTES;
        for (int i = size / 2 - 1; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    public void insert(int key) {
        if (size == capacity) {
            throw new IllegalStateException("Heap is full");
        }
        heap[size] = key;
        arrayAccesses++; // Write to heap[size]
        size++;
        siftUp(size - 1);
    }

    public void increaseKey(int i, int newVal) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        arrayAccesses++; // Access heap[i]
        if (newVal < heap[i]) {
            throw new IllegalArgumentException("New value must be greater than or equal to current");
        }
        heap[i] = newVal;
        arrayAccesses++; // Write to heap[i]
        siftUp(i);
    }

    public int extractMax() {
        if (size <= 0) {
            throw new IllegalStateException("Heap is empty");
        }
        arrayAccesses++; // Access heap[0]
        int max = heap[0];
        arrayAccesses += 2; // Access heap[size-1] and write to heap[0]
        heap[0] = heap[size - 1];
        size--;
        if (size > 0) {
            maxHeapify(0);
        }
        return max;
    }

    public int getMax() {
        if (size <= 0) {
            throw new IllegalStateException("Heap is empty");
        }
        arrayAccesses++;
        return heap[0];
    }

    private void maxHeapify(int i) {
        int left = left(i);
        int right = right(i);
        int largest = i;
        if (left < size) {
            arrayAccesses++; // heap[left]
            comparisons++;
            if (heap[left] > heap[largest]) {
                largest = left;
            }
        }
        if (right < size) {
            arrayAccesses++; // heap[right]
            comparisons++;
            if (heap[right] > heap[largest]) {
                largest = right;
            }
        }
        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
        }
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = parent(i);
            arrayAccesses++; // Access heap[parent]
            comparisons++;
            if (heap[parent] >= heap[i]) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
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
        arrayAccesses += 4; // 2 reads, 2 writes (temp not counted)
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        swaps++;
    }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getMemoryAllocations() { return memoryAllocations; }

    public void resetMetrics() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        memoryAllocations = 0;
    }

    public int getSize() { return size; }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(heap, size));
    }
}