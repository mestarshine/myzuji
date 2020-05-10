package com.myzuji.study.gof23.prototype.deepclone;

public class Client {

    public static void main(String[] args) throws CloneNotSupportedException {
        DeepProtoType deepProtoType = new DeepProtoType();
        deepProtoType.name = "狼山";
        deepProtoType.deepCloneableTarget = new DeepCloneableTarget("大牛", "大牛的类");

        //方式一 完成深拷贝
        DeepProtoType deepProtoType1 = (DeepProtoType) deepProtoType.clone();

        System.out.println(deepProtoType.name + "," + deepProtoType.deepCloneableTarget.cloneName);
        System.out.println(deepProtoType1.name + "," + deepProtoType1.deepCloneableTarget.cloneName);

        // 方式二
        DeepProtoType deepProtoType2 = (DeepProtoType) deepProtoType.deepClone();

        System.out.println(deepProtoType2.name + "," + deepProtoType2.deepCloneableTarget.cloneName);
    }
}
