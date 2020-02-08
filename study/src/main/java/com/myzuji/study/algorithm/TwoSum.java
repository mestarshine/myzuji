package com.myzuji.study.algorithm;

import java.util.Arrays;

/**
 * 两数之和
 * <p>
 * 给定一个整数数组和一个目标值，找出数组中和为目标值的两个数。
 * <p>
 * 你可以假设每个输入只对应一种答案，且同样的元素不能被重复利用。
 *
 * @author shine
 * @date 2020/02/08
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("结果：" + Arrays.toString(result));
    }

    public static int[] twoSum(int[] nums, int target) {
        int max = nums[0];
        int min = nums[0];
        int len = nums.length;

        for (int i = 0; i < len; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
            if (nums[i] < min) {
                min = nums[i];
            }
        }

        int[] indexes = new int[max - min + 1];
        int[] rets = new int[2];

        int other = 0;
        for (int j = 0; j < len; j++) {
            other = target - nums[j];
            if (other < min || other > max) {
                continue;
            }
            if (indexes[other - min] > 0) {
                rets[0] = indexes[other - min] - 1;
                rets[1] = j;
                return rets;
            }
            indexes[nums[j] - min] = j + 1;
        }

        return rets;
    }
}
