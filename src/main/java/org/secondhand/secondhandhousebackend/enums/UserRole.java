package org.secondhand.secondhandhousebackend.enums;



public enum UserRole {
    买家("买家"),
    卖家("卖家"),
    经纪人("经纪人"),
    管理员("管理员");

    private final String chineseName;

    UserRole(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}