package com.myzuji.study.gof23.strategy;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Cat[] cats = {new Cat(3, 3), new Cat(5, 5), new Cat(1, 1)};
        weightSort(cats);
        System.out.println("weight" + Arrays.toString(cats));
        heightSort(cats);
        System.out.println("height" + Arrays.toString(cats));
    }

    public static void weightSort(Cat[] cats) {
        Sorter<Cat> sorter = new Sorter<>();
        sorter.sort(cats, new CatWeightComparator());
    }

    public static void heightSort(Cat[] cats) {
        Sorter<Cat> sorter = new Sorter<>();
        sorter.sort(cats, new CatHeightComparator());
    }
}
