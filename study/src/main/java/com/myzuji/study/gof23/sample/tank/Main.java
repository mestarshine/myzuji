package com.myzuji.study.gof23.sample.tank;

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
