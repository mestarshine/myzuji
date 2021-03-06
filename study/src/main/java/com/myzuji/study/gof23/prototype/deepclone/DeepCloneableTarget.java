package com.myzuji.study.gof23.prototype.deepclone;

import java.io.Serializable;

public class DeepCloneableTarget implements Serializable, Cloneable {

    private static final long serialVersionUID = -902120719181663325L;

    public String cloneName;
    public String cloneClass;


    public DeepCloneableTarget(String cloneName, String cloneClass) {
        this.cloneName = cloneName;
        this.cloneClass = cloneClass;
    }

    //因为该类的属性都是String，因此使用默认的 clone
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
