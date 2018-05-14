package com.ylz.log.elk.manage.bean;

public enum MutilIndexEnum {
    MUTIL_INDEX_ENUM("1", "组合索引/项目索引"),
    // 放着 后期改造
    PROJECT_NUM("1", "项目索引");

    private String code;
    private String desciption;

    MutilIndexEnum(String code, String desciption) {
        this.code = code;
        this.desciption = desciption;
    }

    public String getCode() {
        return code;
    }

    public String getDesciption() {
        return desciption;
    }
}
