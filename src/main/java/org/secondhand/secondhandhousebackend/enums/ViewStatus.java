package org.secondhand.secondhandhousebackend.enums;

public enum ViewStatus {
    待审批("待审批"), // 待审批
    已预约("已预约"), // 已预约
    已取消("已取消"); // 已取消

    private final String description;

    ViewStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据描述获取枚举值
     * @param description 状态描述
     * @return 匹配的枚举值，如果没有找到则返回 null
     */
    public static ViewStatus fromDescription(String description) {
        for (ViewStatus status : ViewStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return null;
    }
}