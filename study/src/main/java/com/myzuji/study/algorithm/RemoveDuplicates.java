package com.myzuji.study.algorithm;

import java.util.Arrays;

/**
 * 从排序数组中删除重复项
 * <p>
 * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 * <p>
 * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
 *
 * @author shine
 * @date 2020/02/08
 */
public class RemoveDuplicates {

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 4, 4, 4};
        System.out.println(removeDuplicates(nums));
    }

    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int lenght = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[lenght] != nums[i]) {
                nums[++lenght] = nums[i];
            }
        }
        System.out.println("修改后的数组：" + Arrays.toString(nums));
        return lenght + 1;

    }
}
