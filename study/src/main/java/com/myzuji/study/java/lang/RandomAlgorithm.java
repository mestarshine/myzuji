package com.myzuji.study.java.lang;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class RandomAlgorithm {

    public static void main(String[] args) {
        int[] srcArray = {0, 1, 2, 3};
        double[] weights = {0.5D, 0.2D, 0.2D, 0.1D};
        getRandomByWeight(srcArray, weights);
        System.out.println(Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault()).toInstant()));
    }

    /**
     * 制定一个待选数组，按照设定的概率，从待选数组中随机选出值，比如：<br>
     * 待选数组是：{0，1，2，3}，要求在其中随机产生一个数，产生0的概率为50%，1为20%，2为20%，3为10% <br>
     *
     * @param srcArray 候选数字数组
     * @param weights  候选数字对应的概率值
     * @return 根据概率值，候选数字数组，获取的数字
     **/
    public static int getRandomByWeight(int[] srcArray, double[] weights) {
        //第一步：计算概率总和，
        double totalWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            totalWeight = totalWeight + weights[i];
        }

        //第二步：计算真正的概率值列表
        int factorTotleLen = 0;
        double[] realWeights;
        //调用方输入的概率列表，是大概估算的比例，相加并不等于100%的情况
        if (totalWeight != 1) {
            realWeights = new double[weights.length];
            for (int i = 0; i < weights.length; i++) {
                double newWeight = weights[i] / totalWeight;
                realWeights[i] = newWeight;
                factorTotleLen = factorTotleLen + (int) (realWeights[i] * 100);
            }
        } else {//调用方传入的概率值列表非常正规，加起来严格等于1
            realWeights = weights;
            for (int i = 0; i < weights.length; i++) {
                factorTotleLen = factorTotleLen + (int) (realWeights[i] * 100);
            }
        }

        //第三步：计算概率值的正式概率占比，同时，根据正式概率列表，计算出随机候选数字的数量
        int[] srcNums = new int[factorTotleLen];

        //第四步：准备阶段，按照权重生成对等数量的候选数字因子
        int j = 0;
        for (int i = 0; i < realWeights.length; i++) {
            int wn = (int) (realWeights[i] * 100);
            for (int k = 0; k < wn; k++) {
                srcNums[j] = srcArray[i];
                j++;
            }

        }

        //第五步：乱序交换，将数组中的元素乱序交换
        shuffle(srcNums);

        //第四步：随机从候选数字中获取结果
        Random random = new Random();
        int randomIndex = random.nextInt(srcNums.length);
        return srcNums[randomIndex];
    }


    /**
     * 数组乱序
     */
    private static void shuffle(int[] srcArray) {
        Random rnd = new Random();
        int size = srcArray.length;

        for (int i = size; i > 1; i--) {
            swap(srcArray, i - 1, rnd.nextInt(i));
        }

    }

    /**
     * 交换数组指定的两个元素的值
     */
    private static void swap(int[] array, int i, int j) {
        array[i] = array[i] ^ array[j];
        array[j] = array[i] ^ array[j];
        array[i] = array[i] ^ array[j];
    }
}
