package com.myzuji.study.gof23.sample.tank;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        TankFrame tankFrame = new TankFrame();

        //初始化敌方坦克
        for (int i = 0; i < 5; i++) {
            tankFrame.tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, tankFrame, Group.BAD));
        }

        while (true) {
            Thread.sleep(25);
            tankFrame.repaint();
        }
    }
}
