package com.zhp.lcmp.Enum;

/**
 * 枚举类，应用安装状态
 *
 * @author ZhaoHP
 * @date 2020/4/6 10:38
 */
public enum ApplicationStatusEnum {

    ALL("yum list", 0),
    CHECK_UPDATE("yum check-update", 1),
    INSTALL("yum install ", 2),
    UPDATE("yum update ",3),
    REMOVE("yum remove ", 4);

    private String name;
    private int index;

    ApplicationStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
