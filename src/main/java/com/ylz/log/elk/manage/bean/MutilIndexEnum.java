package com.ylz.log.elk.manage.bean;

public enum MutilIndexEnum {
    MUTIL_INDEX_ENUM("1", "组合索引/项目索引"),
    // 放着 后期改造
    PROJECT_NUM("1", "项目索引");

    private String type;
    private String desciption;

    MutilIndexEnum(String type, String desciption) {
        this.type = type;
        this.desciption = desciption;
    }

    public String getType() {
        return type;
    }

    public String getDesciption() {
        return desciption;
    }
}
