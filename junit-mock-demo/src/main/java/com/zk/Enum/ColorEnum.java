package com.zk.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorEnum {

    RED("红色"),
    BLUE("蓝色"),
    ;
    private String desc;
}
