package com.myzuji.study.sort;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class HeapSortTest {

    @Test
    @Disabled
    void maxHeapSort() {
        HeapSort heapSort = new HeapSort(new int[]{80, 60, 16, 56, 45, 10, 15, 30, 40, 20});
        heapSort.maxHeapSort();
        System.out.println("大顶堆排序结果：" + Arrays.toString(heapSort.arr));
    }

    @Test
    @Disabled
    void minHeap() {
        HeapSort heapSort = new HeapSort(new int[]{80, 60, 16, 56, 45, 10, 15, 30, 40, 20});
        heapSort.minHeapSort();
        System.out.println("小顶堆排序结果：" + Arrays.toString(heapSort.arr));
    }
}
