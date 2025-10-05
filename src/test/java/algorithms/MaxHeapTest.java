package algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaxHeapTest {
    private MaxHeap<Integer> heap;

    @BeforeEach
    void setUp() {
        heap = new MaxHeap<>(10);
    }

    @Test
    void testInsertAndGetMax() {
        heap.insert(5);
        assertEquals(5, heap.getMax());
        heap.insert(10);
        assertEquals(10, heap.getMax());
        heap.insert(3);
        assertEquals(10, heap.getMax());
    }

    @Test
    void testExtractMax() {
        Integer[] arr = {1, 3, 5, 7, 9};
        heap.buildMaxHeap(arr);
        assertEquals(9, heap.extractMax());
        assertEquals(7, heap.extractMax());
        assertEquals(5, heap.extractMax());
        assertEquals(3, heap.extractMax());
        assertEquals(1, heap.extractMax());
    }

    @Test
    void testIncreaseKey() {
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.increaseKey(2, 10); // Increase 1 to 10
        assertEquals(10, heap.extractMax());
    }

    @Test
    void testBuildMaxHeap() {
        Integer[] arr = {4, 1, 3, 2, 16, 9, 10, 14, 8, 7};
        heap.buildMaxHeap(arr);
        assertEquals(16, heap.getMax());
    }

    @Test
    void testEmptyHeap() {
        assertThrows(IllegalStateException.class, () -> heap.extractMax());
        assertThrows(IllegalStateException.class, () -> heap.getMax());
    }

    @Test
    void testSingleElement() {
        heap.insert(42);
        assertEquals(42, heap.getMax());
        assertEquals(42, heap.extractMax());
    }

    @Test
    void testDuplicates() {
        heap.insert(5);
        heap.insert(5);
        heap.insert(5);
        assertEquals(5, heap.extractMax());
        assertEquals(5, heap.extractMax());
        assertEquals(5, heap.extractMax());
    }

    @Test
    void testInvalidIncreaseKey() {
        heap.insert(10);
        assertThrows(IllegalArgumentException.class, () -> heap.increaseKey(0, 5));
    }

    @Test
    void testDynamicResizing() {
        for (int i = 0; i < 15; i++) {
            heap.insert(i);
        }
        assertEquals(14, heap.getMax());
        assertEquals(15, heap.getSize());
    }
}
