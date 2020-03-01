package com.zk.common.model;

/**
 * Created by zhukai on 2020/3/1
 */
public enum PersonType {
    S("student"),
    N("normal");

    private String type;

    PersonType(String type) {

        this.type = type;
    }

    public String getType(){
        return type;
    }
}
