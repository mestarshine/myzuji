package com.myzuji.study.java.util;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * java.util.Set
 *
 * @author shine
 * @date 2020/03/24
 */
public class SetStudy {

    public static void main(String[] args) {
        new NavigableSetStudy().methodDemo();
    }

    /**
     * NavigableSet表示Java Collection Framework中的可导航集。
     * NavigableSet接口继承自SortedSet接口。
     * 它的行为类似于SortedSet，除了SortedSet的排序机制之外我们还有导航方法。
     * 例如，与SortedSet中定义的顺序相比，NavigableSet接口可以以相反的顺序导航集合。
     *
     * 实现此接口的类是TreeSet和 ConcurrentSkipListSet
     */
    static class NavigableSetStudy{

        private void methodDemo() {
            NavigableSet<Integer> ns = new TreeSet<>();
            ns.add(0);
            ns.add(1);
            ns.add(2);
            ns.add(3);
            ns.add(4);
            ns.add(5);
            ns.add(6);

            // Get a reverse view of the navigable set
            NavigableSet<Integer> reverseNs = ns.descendingSet();
            // Print the normal and reverse views
            System.out.println("Normal order: " + ns);
            System.out.println("Reverse order: " + reverseNs);

            NavigableSet<Integer> threeOrMore = ns.tailSet(3, true);
            System.out.println("3 or  more:  " + threeOrMore);
            System.out.println("lower(3): " + ns.lower(3));
            System.out.println("floor(3): " + ns.floor(3));
            System.out.println("higher(3): " + ns.higher(3));
            System.out.println("ceiling(3): " + ns.ceiling(3));

            System.out.println("pollFirst(): " + ns.pollFirst());
            System.out.println("Navigable Set:  " + ns);

            System.out.println("pollLast(): " + ns.pollLast());
            System.out.println("Navigable Set:  " + ns);

            System.out.println("pollFirst(): " + ns.pollFirst());
            System.out.println("Navigable Set:  " + ns);

            System.out.println("pollFirst(): " + ns.pollFirst());
            System.out.println("Navigable Set:  " + ns);

            System.out.println("pollFirst(): " + ns.pollFirst());
            System.out.println("Navigable Set:  " + ns);

            System.out.println("pollFirst(): " + ns.pollFirst());
            System.out.println("pollLast(): " + ns.pollLast());
        }
    }
}
